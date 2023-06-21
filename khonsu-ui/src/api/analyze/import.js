import request from "@/utils/request";

export function analyzeLocalProject(query) {
  return request({
    url: 'analyze/import/local',
    method: 'post',
    data: JSON.stringify(query)
  })
}
