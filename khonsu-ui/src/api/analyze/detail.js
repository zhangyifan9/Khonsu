import request from '@/utils/request';

export function getReleaseByRepoName(repoName) {
    return request({
        url: '/analyze/detail/release',
        method: 'get',
        params: { repoName }
    })
}

export function getCodeFile(userName, repoName, tag, path) {
  return request({
    url: '/analyze/detail/file',
    method: 'get',
    params: {
      userName,
      repoName,
      tag,
      path
    }
  })
}


export function updateState(releaseId, state) {
  return request({
    url: '/analyze/detail/state',
    method: 'get',
    params: {
      releaseId,
      state
    }
  })
}

export function updateScore(releaseId, codeBlockId, score) {
  return request({
    url: '/analyze/detail/score',
    method: 'get',
    params: {
      releaseId,
      codeBlockId,
      score
    }
  })
}

export function getAvg(repoName, tag, isTop, count) {
  return request({
    url: '/analyze/detail/avg',
    method: 'get',
    params: {
      repoName,
      tag,
      isTop,
      count
    }
  })
}

export function getBase(userName, repoName, last) {
  return request({
    url: '/analyze/detail/base',
    method: 'get',
    params: {
      userName,
      repoName,
      last
    }
  })
}

// export function getCodeByReleaseId(releaseId, pageNum, pageSize) {
//     return request({
//         url: '/analyze/detail/code',
//         method: 'get',
//         params: {
//             releaseId,
//             pageNum,
//             pageSize
//         }
//     })
// }

export function getCodeByReleaseId(releaseId) {
  return request({
    url: '/analyze/detail/code',
    method: 'get',
    params: {
      releaseId
    }
  })
}

export function getClassPath(repoName) {
  return request({
    url: '/analyze/detail/aggregation',
    method: 'get',
    params: {
      repoName
    }
  })
}

export function getCore(repoName, tag) {
  return request({
    url: '/analyze/detail/core',
    method: 'get',
    params: {
      repoName,
      tag
    }
  })
}

