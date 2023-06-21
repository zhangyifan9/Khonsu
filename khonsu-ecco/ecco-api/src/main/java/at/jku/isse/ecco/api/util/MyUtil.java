package at.jku.isse.ecco.api.util;

import org.apache.commons.codec.binary.Base32;
import redis.clients.jedis.Jedis;

import java.util.Base64;
import java.util.Map;

/**
 * @ClassName MyUtil
 * @Description TODO
 * @Author panpan
 */
public class MyUtil {

    // 对字符串进行预处理编码使其能够成为文件名
    public static String base64Encode(String name) {
        byte[] encodedBytes = name.getBytes();
        return Base64.getEncoder().encodeToString(encodedBytes);
    }

    // 对文件名进行解码还原
    public static String base64Decode(String encodedStr) {
        return new String(Base64.getDecoder().decode(encodedStr));
    }

    public static String base32Encode(String str) {
        byte[] bytes = str.getBytes();
        Base32 base32 = new Base32();
        return base32.encodeAsString(bytes);
    }

    public static String base32Decode(String encodedStr) {
        Base32 base32 = new Base32();
        return new String(base32.decode(encodedStr));
    }

    public static void main(String[] args) {
        String encoded64 = base64Encode("releases/lucene/9.5.0");
        System.out.println(base64Decode(encoded64));
        String encoded32 = base32Encode("d^0(450) OR d^0(460) OR d^0(451) OR d^0(461) OR d^0(440) OR d^0(420) OR d^0(421) OR d^0(430) OR d^0(410) OR d^0(431)");
        System.out.println(encoded32);
        System.out.println(base32Decode(encoded32).length());
        Jedis jedis = new Jedis("localhost", 6379);
        // jedis.configSet("save", "900 1 300 10");
        // jedis.hset("lucene", String.valueOf(1), "001");
        // jedis.hset("lucene", String.valueOf(2), "002");
        // jedis.hset("lucene", String.valueOf(3), "003");

        Map<String, String> query = jedis.hgetAll("lucene");

        System.out.println(query.size());
        jedis.close();

    }
}
