import request from "@/utils/request";

export function getProject() {
    return request({
        url: '/index/project',
        method: 'get',
    })
}

export function updateProject(id) {
    return request({
        url: '/index/update',
        method: 'get',
        params: {
            id
        }
    })
}
