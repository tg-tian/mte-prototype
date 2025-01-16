<template>
  <div>
    <div style="padding:  20px; " >
      <el-form
          :rules="rules"
          :model="deviceForm"
          ref = "deviceFormRef"
          label-width="auto"
          label-position="left"
          style="max-width: 800px;margin: auto;"
      >
        <el-form-item label="设备码" prop="deviceCode">
          <el-input v-model="deviceForm.deviceCode" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="deviceForm.deviceName" placeholder="请输入"/>
        </el-form-item>
        <el-form-item label="图标上传" prop="imageUrl">
          <el-upload
              drag
              multiple
              ref="uploadRef"
              class="avatar-uploader"
              :show-file-list="true"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarUpload"
              :action="`${uploadUrl}?deviceCode=${deviceForm.deviceCode}`"
              :auto-upload="false"
          >
            <el-image v-if="deviceForm.imageUrl" :src="getDeviceImage(deviceForm.imageUrl)" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><UploadFilled /></el-icon>
            <div class="el-upload__text">
              将文件拖入框中或者 <em>点击上传</em>
            </div>
            <div class="el-upload__tip">
              jpg/png 文件大小应小于2MB
            </div>

          </el-upload>
        </el-form-item>

          <div class="domain-subtitle" style="display: flex;justify-content: space-between">
            <el-button type="primary" @click="submitForm(deviceFormRef)" style="margin-left: auto;" >
              下一步
            </el-button>
            <el-button @click="resetForm" style="margin-right: auto;">重置</el-button>
          </div>
      </el-form>
    </div>

  </div>
</template>

<script setup lang= "ts">

import {ElMessage, FormInstance, FormRules, UploadProps,UploadInstance} from "element-plus";
import { UploadFilled } from '@element-plus/icons-vue'
import { getDeviceImage } from "@/utils/pub-use";

const uploadRef = ref<UploadInstance>()
const uploadUrl=import.meta.env.VITE_BASE_PATH+"/file/upload"

//数据基本信息暂存数据结构
/**
 *
 * el-form-item 组件中的 prop 属性用于指定绑定的数据属性，以便与表单验证和错误提示关联。具体来说，prop 属性在以下几个方面起作用：
 * 表单验证：
 * 当 el-form-item 包含 prop 属性时，el-form 组件会根据验证规则对该属性进行验证。验证规则通常在 el-form 组件的 rules 属性中定义。prop 属性指定了该表单项对应的数据属性，这样可以将验证规则与数据属性关联起来。
 * 错误提示：
 * 当验证失败时，el-form-item 会显示相应的错误提示信息。通过 prop 属性，el-form-item 知道需要显示哪个属性的错误信息。
 * */
interface RuleForm{
  deviceCode: string;
  deviceName: string;
  // icon_upload:any;
  //deviceType:string;
  imageUrl:string;
}

const emit = defineEmits(['update-info'])

const props = defineProps({
  info: Object,
});

const deviceFormRef = ref<FormInstance>()
//验证规则,form-item中使用prop数据属性绑定此处的验证规则
const rules = reactive<FormRules<RuleForm>>({
  deviceCode:[
    {required: true, message:'请输入设备码', trigger:'blur'},
  ],
  deviceName:[
    {required: true, message:'请输入设备名称', trigger:'blur'},
  ],
  // imageUrl:[
  //   {required: true, message:'请输入上传图标', trigger:'blur'},
  // ]
})

const deviceForm = reactive<RuleForm>({
deviceCode: "",
deviceName: "",
imageUrl: ""
})
watchEffect(() => {
  if (props.info) {
    deviceForm.deviceCode = props.info.deviceCode
    deviceForm.deviceName = props.info.deviceName
    deviceForm.imageUrl = props.info.imageUrl
  }
})
const resetForm = () => {
  if(deviceFormRef){
    deviceFormRef.value.resetFields()
  }
  return
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return
  await formEl.validate((valid, fields) => {
      if (valid) {
        uploadRef.value!.submit()
        emit('update-info',deviceForm)
        console.log('submit!', fields, deviceForm)
      }
  })
}

//控制所上传图像的大小
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  console.log(rawFile.type)
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/jpg' && rawFile.type !== 'image/png') {
    ElMessage.error('Picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Picture size can not exceed 2MB!')
    return false
  }
  return true
}
const handleAvatarUpload: UploadProps['onSuccess'] = (res)=>{
  deviceForm.imageUrl = res;
  console.log(deviceForm.imageUrl);
}

</script>

<style >
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
   width: 450px;
   position: relative;
   overflow: hidden;
   margin-left: 100px;
   margin-top:  20px;
   transition: var(--el-transition-duration-fast);
 }

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}


</style>
