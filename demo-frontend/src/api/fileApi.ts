import request from "@/utils/request";

export const getFileData = (filePath: String) =>
    request({
        url: `/data?filePath=${filePath}`,
        method: 'get'
    })

export const saveFileData = (filePath: String, content: String) =>
    request({
        url: `/file/save`,
        method: 'post',
        data: {
            filePath: filePath,
            content: content
        }
    })
