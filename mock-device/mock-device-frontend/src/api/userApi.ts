import request from '../utils/request'

// 登录
export const getLogin = (params: any) =>
  request({
    url: '/login',
    method: 'post',
    data: params
  })

// 注册
export const register = (params: any) =>
  request({
    url: '/student',
    method: 'post',
    data: params
  })

// 查看个人信息
export const getMyProfile = (stuNum: string) =>
  request({
    url: `/student/${stuNum}`,
    method: 'get'
  })
