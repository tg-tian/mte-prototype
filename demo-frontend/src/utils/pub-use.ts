export const getAssetsFile = (url: string)=>{
    return new URL(`../assets/${url}`, import.meta.url).href
}

export const getDeviceImage = (url: string)=>{
    return import.meta.env.VITE_BASE_PATH + url
}
