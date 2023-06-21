import request from "@/utils/request";

export function displayTags(userName, repoName) {
    return request({
        url: 'analyze/new/show',
        method: 'get',
        params: {
            userName,
            repoName
        }
    })
}

export function getTags(userName, repoName, tag1, tag2) {
    return request({
        url: 'analyze/new/tag',
        method: 'get',
        params: {
            userName,
            repoName,
            tag1,
            tag2
        }
    })
}

export function getRNs(userName, repoName, tags) {
    return request({
        url: 'analyze/new/rn',
        method: 'get',
        params: {
            userName,
            repoName,
            tags
        }
    })
}

export function analyze(query) {
    return request({
        url: 'analyze/new/start',
        method: 'post',
        data: JSON.stringify(query)
    })
}
