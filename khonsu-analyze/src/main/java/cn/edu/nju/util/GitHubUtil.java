package cn.edu.nju.util;

import cn.edu.nju.domain.structure.GHRN;
import cn.edu.nju.domain.structure.GHRNCategory;
import cn.edu.nju.domain.vo.TagVO;
import cn.edu.nju.domain.vo.TagWithDateVO;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kohsuke.github.*;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName GithubUtil
 * @Description TODO
 * @Author panpan
 */
public class GitHubUtil {
    private static final String msg = "该版本非发布版本或 GitHub 上无版本发布说明";

    // 记录tags的顺序(不完全字典序)以备后续使用
    public static List<String> sortedTags = new ArrayList<>();

    public static List<TagWithDateVO> getTagWithDateList(String userName, String repoName, String token) throws IOException {
        sortedTags.clear();
        List<TagWithDateVO> res = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        GHRepository repository = github.getRepository(userName + "/" + repoName);
        List<GHTag> tags = repository.listTags().asList();
        for (GHTag tag : tags) {
            TagWithDateVO tagWithDateVO = new TagWithDateVO(tag.getName(), formatter.format(tag.getCommit().getCommitDate()));
            res.add(tagWithDateVO);
            sortedTags.add(tag.getName());
            System.out.println(tagWithDateVO);
        }
        GitHub.offline();
        return res;
    }

    public static List<TagVO> getTagInfo(String userName, String repoName, String tag1, String tag2, String token) throws IOException {
        List<TagVO> tagVOs = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        GHRepository repository = github.getRepository(userName + "/" + repoName);
        List<GHTag> ghTags = repository.listTags().asList();
        int index1 = sortedTags.indexOf(tag1);
        int index2 = sortedTags.indexOf(tag2);
        for (int i = index1; i <= index2; i++) {
            GHTag tag = ghTags.get(i);
            boolean isRelease = true;
            try {
                GHRef ghRef = repository.getRef("tags/" + tag.getName());
                GHTagObject ghTagObject = repository.getTagObject(ghRef.getObject().getSha());
                GHRelease release = repository.getReleaseByTagName(tag.getName());
                if (release == null)
                    isRelease = false;
                System.out.println(tag.getName());
                tagVOs.add(new TagVO(tag.getName(), ghTagObject.getMessage(), isRelease, ghTagObject.getTagger().getName(), ghTagObject.getTagger().getEmail(), formatter.format(ghTagObject.getTagger().getDate())));
            } catch (FileNotFoundException e) {
                tagVOs.add(new TagVO(tag.getName(), "none" , false, "none", "none", "none"));
                System.out.println(tag.getName() + "有问题，忽略");
            }
        }
        return tagVOs;
    }

    public static String getReleaseNote(String userName, String repoName, String tagName, String token) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        GHRepository repository = github.getRepository(userName + "/" + repoName);
        GHRelease release = repository.getReleaseByTagName(tagName);
        // tag 不一定是 release，但 GitHub 上也并非完全准确，可能需要自行寻找 release note
        if (release == null) return msg;
        else if (release.getBody().length() == 0) return msg;

        String body = release.getBody();
        GitHub.offline();
        // return GFMToPlainText(userName, repoName, body);
        List<GHRN> ghrns = new ArrayList<>();
        ghrns.add(gfmRNToGHRN(userName, repoName, tagName, body));
        return combineGHRNsTxt(ghrns);
    }

    public static String combineGHRNsTxt(List<GHRN> ghrns) {
        List<String> rnsList = new ArrayList<>();
        for (GHRN ghrn : ghrns) {
            List<String> rnList = new ArrayList<>();
            rnList.add(ghrn.getTag());
            for (GHRNCategory category : ghrn.getCategories()) {
                List<String> categoryList = new ArrayList<>();
                categoryList.add("## " + category.getCategory() + " ##");
                for (String content : category.getRns()) {
                    categoryList.add(content);
                }
                rnList.add(String.join("\n", categoryList));
            }
            rnsList.add(String.join("\n========\n", rnList));
        }
        return String.join("\n============================\n", rnsList);
    }

    public static void splitGHRNsTxt(String allRNs, String repoName, String path) {
        String localPath = path + "\\release\\" + repoName;
        // 检查该路径是否存在
        File projectFile = new File(localPath);
        if (projectFile.exists()) {
            try {
                FileUtils.deleteDirectory(projectFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Jedis jedis = new Jedis("localhost", 6379);
        String[] rns = allRNs.split("\n============================\n");
        int count = 0;
        for (String rn : rns) {
            String[] tag_categories = rn.split("\n========\n");
            String tag_num = jedis.hget(repoName, tag_categories[0]);

            for (int i = 1; i < tag_categories.length; i++) {
                String[] category_contents = tag_categories[i].split("\n");
                String category = category_contents[0].replaceAll("#", "").trim();
                for (int j = 1; j < category_contents.length; j++) {
                    File f = new File(localPath + "\\" + tag_num + "\\" + category);
                    f.mkdirs();
                    try {
                        FileWriter fileWriter = new FileWriter(localPath + "\\" + tag_num + "\\" + category + "\\r" + count + ".txt");
                        fileWriter.write(category_contents[j]);
                        count++;
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        jedis.close();
    }

    public static GHRN gfmRNToGHRN(String userName, String repoName, String tagName, String gfmRN) {
        GHRN ghrn = new GHRN(tagName, new ArrayList<>());
        // // 匹配gfmRN中存在的issue或pr编号
        // Pattern pnum = Pattern.compile("(#)(\\d+)");
        // 匹配gfmRN中存在的issue或pr链接
        Pattern plink = Pattern.compile("(https?://github\\.com/" + userName + "/" + repoName + "/)(issues|pull)/(\\d+)");
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(parser.parse(gfmRN));
        Document doc = Jsoup.parse(html);
        // 寻找字号最小的标题元素
        int maxHNum = 0;
        Elements hElements = doc.select("h1, h2, h3, h4, h5, h6");
        for (Element hElement : hElements) {
            int hNum = Integer.parseInt(hElement.tagName().substring(1));
            if (hNum > maxHNum) {
                maxHNum = hNum;
            }
        }
        Elements headings = doc.select("h" + maxHNum); // 获取所有的<hmaxHNum>标签
        if (!headings.isEmpty()) {
            // 有分类标题
            for (Element heading : headings) {
                Element list = heading.nextElementSibling(); // 获取当前<h>标签的下一个兄弟元素，即<ul>标签
                if (list.tagName().equals("ul")) {
                    // contributor不要
                    if (heading.text().toLowerCase().contains("contributor")) continue;
                    String category = heading.text().trim(); // 获取<h>标签的文本内容
                    // 分类名不应该含有非字母
                    category = category.replaceAll(":\\w+:", "").replaceAll("[^a-zA-Z\\s]", "").trim();
                    GHRNCategory ghrnCategory = new GHRNCategory(category, new ArrayList<>());
                    Elements items = list.select("li"); // 获取所有的<li>标签
                    for (Element item : items) {
                        List<String> urls = new ArrayList<>();
                        String text = item.text(); // 获取<li>标签的文本内容
                        Matcher linkMatcher = plink.matcher(text);
                        while (linkMatcher.find()) {
                            text = linkMatcher.replaceAll("#" + linkMatcher.group(3));
                        }
                        // Matcher numMatcher = pnum.matcher(text);
                        // while (numMatcher.find()) {
                        //     urls.add("https://github.com/" + userName + "/" + repoName + "/issues/" + numMatcher.group(2));
                        // }
                        ghrnCategory.getRns().add(text);
                        // if (urls.size() == 0) {
                        //     ghrnCategory.getRns().add(text);
                        // } else {
                        //     ghrnCategory.getRns().add(text + "(" + String.join(", ", urls) + ")");
                        // }
                    }
                    ghrn.getCategories().add(ghrnCategory);
                } else {
                    // 没用列表表示的不要
                }
            }

        } else if (!doc.select("li").isEmpty()) {
            GHRNCategory ghrnCategory = new GHRNCategory("Non-classified", new ArrayList<>());
            Elements items = doc.select("li"); // 获取所有的<li>标签
            for (Element item : items) {
                List<String> urls = new ArrayList<>();
                String text = item.text(); // 获取<li>标签的文本内容
                Matcher linkMatcher = plink.matcher(text);
                while (linkMatcher.find()) {
                    text = linkMatcher.replaceAll("#" + linkMatcher.group(3));
                }
                // Matcher numMatcher = pnum.matcher(text);
                // while (numMatcher.find()) {
                //     urls.add("https://github.com/" + userName + "/" + repoName + "/issues/" + numMatcher.group(2));
                // }
                ghrnCategory.getRns().add(text);
                // if (urls.size() == 0) {
                //     ghrnCategory.getRns().add(text);
                // } else {
                //     ghrnCategory.getRns().add(text + "(" + String.join(", ", urls) + ")");
                // }
            }
            ghrn.getCategories().add(ghrnCategory);
        } else {
            GHRNCategory category = new GHRNCategory("Re", new ArrayList<>());
            category.getRns().add(gfmRN);
            ghrn.getCategories().add(category);
        }

        return ghrn;
    }

    /**
     * 聚类了一下GitHub上release的种类
     * (处理)1.有分类，且每个分类列表下都用"·"作为条目的开始，见spring-framework
     * (处理)2.无分类，所有的条目都在一个列表里，见codecat
     * (不处理)3.只给链接，用一个段落来展示，见lucene
     */
    private static String GFMToPlainText(String userName, String repoName, String markdownText) {
        // 替换链接
        Pattern pattern1 = Pattern.compile("(https?://github\\.com/\\S+/\\S+/)(issues|pull)/(\\d+)");
        // 匹配编号
        Pattern pattern2 = Pattern.compile("(#)(\\d+)");
        List<String> ss = new ArrayList<>();
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(parser.parse(markdownText));
        Document doc = Jsoup.parse(html);
        int maxHNum = 0;
        Elements hElements = doc.select("h1, h2, h3, h4, h5, h6");
        for (Element hElement : hElements) {
            int hNum = Integer.parseInt(hElement.tagName().substring(1));
            if (hNum > maxHNum) {
                maxHNum = hNum;
            }
        }
        Elements headings = doc.select("h" + maxHNum); // 获取所有的<h2>标签
        if (!headings.isEmpty()) {
            // 有分类标题
            for (Element heading : headings) {
                Element list = heading.nextElementSibling(); // 获取当前<h2>标签的下一个兄弟元素，即<ul>标签
                if (list.tagName().equals("ul")) {
                    if (heading.text().toLowerCase().contains("contributor")) continue;
                    StringBuilder sb = new StringBuilder();
                    String title = heading.text().trim(); // 获取<h2>标签的文本内容
                    title = "## " + title.replaceAll(":\\w+:", "").replaceAll("[^a-zA-Z\\s]", "").trim() + " ##";
                    sb.append(title + "\n"); // 输出标题
                    Elements items = list.select("li"); // 获取所有的<li>标签
                    for (Element item : items) {
                        List<String> urls = new ArrayList<>();
                        String text = item.text(); // 获取<li>标签的文本内容
                        Matcher matcher2 = pattern2.matcher(text);
                        while (matcher2.find()) {
                            urls.add("https://github.com/" + userName + "/" + repoName + "/issues/" + matcher2.group(2));
                        }
                        Matcher matcher1 = pattern1.matcher(text);
                        while (matcher1.find()) {
                            urls.add(matcher1.group(0));
                            text = matcher1.replaceAll("#" + matcher1.group(3));
                        }
                        sb.append(text); // 输出每个条目的文本
                        if (urls.size() == 0) {
                            sb.append("\n");
                        } else {
                            sb.append("(" + String.join(",", urls) + ")\n");
                        }
                    }
                    ss.add(sb.toString());
                } else {
                    // TODO 大概率是致谢所以不做处理，如果出现特殊情况，看这里
                }
            }

        } else if (!doc.select("li").isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Elements items = doc.select("li"); // 获取所有的<li>标签
            sb.append("Non-classified\n");
            for (Element item : items) {
                List<String> urls = new ArrayList<>();
                String text = item.text(); // 获取<li>标签的文本内容
                Matcher matcher2 = pattern2.matcher(text);
                while (matcher2.find()) {
                    urls.add("https://github.com/" + userName + "/" + repoName + "/issues/" + matcher2.group(2));
                }
                Matcher matcher1 = pattern1.matcher(text);
                while (matcher1.find()) {
                    urls.add(matcher1.group(0));
                    text = matcher1.replaceAll("#" + matcher1.group(3));
                }
                sb.append(text); // 输出每个条目的文本
                if (urls.size() == 0) {
                    sb.append("\n");
                } else {
                    sb.append("(" + String.join(", ", urls) + ")\n");
                }
            }
            ss.add(sb.toString());
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(markdownText + "\n");
            ss.add(sb.toString());
        }

        return String.join("========\n", ss);
    }

    // 顺推一个版本作为base
    public static boolean downloadSpecificVersions(String userName, String repoName, List<String> tags, String path, String token) {
        // 必须有一个base版本
        if (sortedTags.indexOf(tags.get(tags.size() - 1)) == sortedTags.size() - 1) return false;

        Jedis jedis = new Jedis("localhost", 6379);

        Collections.sort(tags, Comparator.comparingInt(a -> sortedTags.indexOf(a)));

        // 下载速度较快的代理
        String proxy = "https://ghproxy.com/";
        String localPath = path + "\\java\\" + repoName;
        String githubPath = "https://github.com/" + userName + "/" + repoName + ".git";

        // 检查该路径是否存在
        File projectFile = new File(localPath);
        if (projectFile.exists()) {
            try {
                FileUtils.deleteDirectory(projectFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(localPath);
        System.out.println(githubPath);
        String basePath = localPath + "\\base";
        File baseFile = new File(basePath);

        System.out.println("开始clone最新版本");
        long startTime = System.currentTimeMillis();

        try {
            CredentialsProvider provider = new UsernamePasswordCredentialsProvider(token, "");
            Git git = Git.cloneRepository()
                    .setCredentialsProvider(provider)
                    .setURI(proxy + githubPath)
                    .setDirectory(baseFile)
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();

            long timeCost = System.currentTimeMillis() - startTime;

            System.out.println("花费时间: " + timeCost);

            // 回滚并复制指定版本
            Set<String> tagSet = new HashSet<>();
            for (String tag : tags) tagSet.add(tag);

            jedis.del(repoName);
            jedis.del(repoName + "-hash");
            jedis.configSet("save", "900 1 300 10");

            GitHub github = new GitHubBuilder().withOAuthToken(token).build();
            GHRepository repository = github.getRepository(userName + "/" + repoName);
            List<GHTag> allTags = repository.listTags().asList();
            GitHub.offline();

            int i = 0;
            int start = sortedTags.indexOf(tags.get(0));
            int end = sortedTags.indexOf(tags.get(tags.size() - 1));

            for (int j = start; j <= end; j++) {
                GHTag tag = allTags.get(j);
                String name = tag.getName();
                if (tagSet.contains(name)) {
                    String versionName = String.valueOf(i);
                    jedis.hset(repoName, String.valueOf(i), name);
                    jedis.hset(repoName, name, String.valueOf(i));
                    jedis.hset(repoName + "-hash", name, tag.getCommit().getSHA1());
                    i++;

                    String historyVersionPath = localPath + "\\non-base\\" + versionName;
                    File historyVersionFile = new File(historyVersionPath);
                    historyVersionFile.mkdir();

                    System.out.println("切换到" + name);

                    // base回滚
                    String sha = tag.getCommit().getSHA1();
                    // git.reset().setMode(ResetCommand.ResetType.HARD).setRef(sha).call();
                    git.checkout().setName(sha).call();

                    // 复制
                    FileUtils.copyDirectory(new File(basePath), new File(historyVersionPath));

                    System.out.println("path: " + historyVersionPath);
                }
            }

            jedis.hset(repoName, String.valueOf(i), "base");
            jedis.hset(repoName, "base", String.valueOf(i));
            // base版本处理
            git.checkout().setName(allTags.get(end + 1).getCommit().getSHA1()).call();

            git.close();
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 传进来的allRNs（格式约束）
     * 每个版本间用"\n============================\n"分割
     * 版本和每个分类间用"\n========\n"分割
     * 分类和每个issue用"\n"分割
     */
    public static void splitRNs(String allRNs, String repoName, String path) {
        String localPath = path + "\\release\\" + repoName;
        // 检查该路径是否存在
        File projectFile = new File(localPath);
        if (projectFile.exists()) {
            try {
                FileUtils.deleteDirectory(projectFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Jedis jedis = new Jedis("localhost", 6379);
        String[] rns = allRNs.split("\n============================\n");
        int count = 0;
        for (String rn : rns) {
            String[] tag_categories = rn.split("\n========\n");
            String tag_num = jedis.hget(repoName, tag_categories[0]);

            for (int i = 1; i < tag_categories.length; i++) {
                String[] category_contents = tag_categories[i].split("\n");
                String category = category_contents[0].replaceAll("#", "").trim();
                for (int j = 1; j < category_contents.length; j++) {
                    File f = new File(localPath + "\\" + tag_num + "\\" + category);
                    f.mkdirs();
                    try {
                        FileWriter fileWriter = new FileWriter(localPath + "\\" + tag_num + "\\" + category + "\\r" + count + ".txt");
                        fileWriter.write(category_contents[j]);
                        count++;
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        jedis.close();
    }

    public static String getBaseTag(String userName, String repoName, String lastTag, String token) throws IOException {
        GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        GHRepository repository = github.getRepository(userName + "/" + repoName);
        List<GHTag> allTags = repository.listTags().asList();
        GitHub.offline();

        int i = 0;
        for (; i < allTags.size(); i++) {
            if (allTags.get(i).getName().equals(lastTag)) break;
        }
        return allTags.get(i + 1).getName();
    }

    public static void main(String[] args) throws IOException, GitAPIException, URISyntaxException {
        String token = "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";
        String path = "D:\\Workplace\\nju\\tmp";

        // String userName = "apache";
        // // String repoName = "spring-boot";
        // String repoName = "rocketmq";
        //
        // String tag = "rocketmq-all-5.1.0";

        // String userName = "skylot";
        // // String repoName = "spring-boot";
        // String repoName = "jadx";
        //
        // String tag = "v1.4.6";

        // String userName = "jenkinsci";
        // // String repoName = "spring-boot";
        // String repoName = "jenkins";
        //
        // String tag = "jenkins-2.398";
        // String userName = "oblac";
        // String repoName = "jodd";
        //
        // String tag = "v5.1.4";

        // splitRNs(getReleaseNote(userName, repoName, tag, token), "jodd", "");

        // String tags = "v2.7.9&v2.7.8&v2.7.7&v2.7.6&v2.7.5&v2.7.4&v2.7.3&v2.7.2&v2.7.1";

        // String userName = "google";
        // String repoName = "gson";
        // String tags = "gson-parent-2.9.1&gson-parent-2.9.0&gson-parent-2.8.9&gson-parent-2.8.8";
        //
        // GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        // GHRepository repository = github.getRepository(userName + "/" + repoName);
        // List<GHTag> ghTags = repository.listTags().asList();
        // GitHub.offline();
        // for (GHTag ghTag : ghTags) sortedTags.add(ghTag.getName());
        //
        // downloadSpecificVersions(userName, repoName, Arrays.asList(tags.split("&")), path, token);

        // String owner = "google";
        // String repoName = "gson";
        // String filePath = "gson/gson/src/test/java/com/google/gson/functional/TypeVariableTest.java";
        // String version = "gson-parent-2.9.1"; // The version you want to check
        //
        // GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        // GHRepository repository = github.getRepository(owner + "/" + repoName);
        // List<GHTag> ghTags = repository.listTags().asList();
        // System.out.println();

        // String userName = "apache";
        // String repoName = "dubbo";
        // String tagName = "dubbo-3.1.9";
        // // getTagInfo(userName, repoName, "dubbo-3.1.9", "dubbo-3.1.9", token);
        //
        // List<TagVO> tagVOs = new ArrayList<>();
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // GitHub github = new GitHubBuilder().withOAuthToken(token).build();
        // GHRepository repository = github.getRepository(userName + "/" + repoName);
        // GHRef[] ghRefs = repository.getRefs();
        // List<GHTag> ghTags = repository.listTags().asList();
        // for (GHTag tag : ghTags) {
        //     if (tag.getName().equals(tagName)) {
        //         try {
        //             GHTagObject ghTagObject = repository
        //                     .getTagObject(repository.getRef("tags/" + tag.getName()).getObject().getSha());
        //             GHRelease release = repository.getReleaseByTagName(tag.getName());
        //             // if (release == null)
        //             //     isRelease = false;
        //             // System.out.println(tag.getName());
        //             // tagVOs.add(new TagVO(tag.getName(), ghTagObject.getMessage(), isRelease, ghTagObject.getTagger().getName(), ghTagObject.getTagger().getEmail(), formatter.format(ghTagObject.getTagger().getDate())));
        //         } catch (Exception e) {
        //             System.out.println();
        //             // System.out.println(tag.getName() + "有问题，忽略");
        //         }
        //     }
        //     boolean isRelease = true;
        // }



    }
}
