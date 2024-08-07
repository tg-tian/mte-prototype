<template>
  <el-upload
      drag
      class="avatar-uploader"
      :show-file-list="false"
      :before-upload="beforeAvatarUpload"
  >
    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><UploadFilled /></el-icon>
    <div class="el-upload__text">
      将文件拖入框中或者 <em>点击上传</em>
    </div>
    <div class="el-upload__tip">
      jpg/png 文件大小应小于2MB
    </div>
  </el-upload>
</template>

<script  setup lang = "ts">

import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'

const imageUrl = ref('')
//控制所上传图像的大小
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Picture size can not exceed 2MB!')
    return false
  }
  return true
}

</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  width: 478px;
  position: relative;
  overflow: hidden;
  margin: auto;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}


</style>