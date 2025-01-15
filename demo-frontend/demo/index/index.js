import {d as defineComponent,c as createBlock,w as withCtx,u as unref,o as openBlock,K as KeepAlive,r as resolveDynamicComponent,R as RouterView,a as api,b as axios,E as ElMessage,e as onMounted,f as reactive,t as toRefs,g as computed,h as createElementBlock,i as createBaseVNode,j as toDisplayString,k as onActivated,l as ref,m as createVNode,F as Fragment,n as renderList,p as withDirectives,q as useRouter,s as nextTick,v as ElBreadcrumb,x as ElButton,y as vLoading,z as createTextVNode,A as arrow_right_default,B as createCommentVNode,C as ElBreadcrumbItem,D as ElInput,G as ElOption,H as ElSelect,I as ElFormItem,J as ElForm,L as isRef,M as ElDialog,N as ElTable,O as ElTableColumn,P as defineStore,Q as storeToRefs,S as watchEffect,T as monitor_default,U as user_default,V as compass_default,W as data_board_default,X as house_default,Y as setting_default,Z as ElIcon,_ as ElMenuItem,$ as ElSubMenu,a0 as ElMenu,a1 as pushScopeId,a2 as popScopeId,a3 as watch,a4 as ElDropdownItem,a5 as ElDropdownMenu,a6 as ElDropdown,a7 as resolveComponent,a8 as ElContainer,a9 as ElHeader,aa as ElAside,ab as ElMain,ac as vShow,ad as mergeProps,ae as more_default,af as normalizeClass,ag as ElCheckbox,ah as ElImage,ai as caret_bottom_default,aj as caret_top_default,ak as ElTabs,al as plus_default,am as ElTabPane,an as upload_filled_default,ao as ElUpload,ap as ElSteps,aq as arrow_left_default,ar as picture_default,as as edit_default,at as ElStep,au as useRoute,av as ElDescriptions,aw as ElProgress,ax as ElDescriptionsItem,ay as search_default,az as ElRow,aA as ElCol,aB as ElAnchorLink,aC as ElAnchor,aD as connection_default,aE as cpu_default,aF as document_default,aG as share_default,aH as eleme_default,aI as menu_default,aJ as ElTag,aK as ElRadio,aL as ElRadioGroup,aM as ace,aN as __CJS__import__1__,aO as themeGithub,aP as themeChrome,aQ as themeMonokai,aR as modeJson,aS as modeJavascript,aT as modeHtml,aU as modePython,aV as workerBase,aW as workerJson,aX as workerJavascript,aY as workerHtml,aZ as json,a_ as javascript,a$ as html,b0 as python,b1 as extSearchbox,b2 as _interopRequireDefault$1,b3 as __CJS__import__0__,b4 as createRouter,b5 as createWebHashHistory,b6 as createApp,b7 as createPinia,b8 as src_default,b9 as installer}from'./js/vendor.js';true&&(function polyfill() {
    const relList = document.createElement('link').relList;
    if (relList && relList.supports && relList.supports('modulepreload')) {
        return;
    }
    for (const link of document.querySelectorAll('link[rel="modulepreload"]')) {
        processPreload(link);
    }
    new MutationObserver((mutations) => {
        for (const mutation of mutations) {
            if (mutation.type !== 'childList') {
                continue;
            }
            for (const node of mutation.addedNodes) {
                if (node.tagName === 'LINK' && node.rel === 'modulepreload')
                    processPreload(node);
            }
        }
    }).observe(document, { childList: true, subtree: true });
    function getFetchOpts(link) {
        const fetchOpts = {};
        if (link.integrity)
            fetchOpts.integrity = link.integrity;
        if (link.referrerPolicy)
            fetchOpts.referrerPolicy = link.referrerPolicy;
        if (link.crossOrigin === 'use-credentials')
            fetchOpts.credentials = 'include';
        else if (link.crossOrigin === 'anonymous')
            fetchOpts.credentials = 'omit';
        else
            fetchOpts.credentials = 'same-origin';
        return fetchOpts;
    }
    function processPreload(link) {
        if (link.ep)
            // ep marker = processed
            return;
        link.ep = true;
        // prepopulate the load record
        const fetchOpts = getFetchOpts(link);
        fetch(link.href, fetchOpts);
    }
}());const _sfc_main$P = /* @__PURE__ */ defineComponent({
  __name: "App",
  setup(__props) {
    return (_ctx, _cache) => {
      return openBlock(), createBlock(unref(RouterView), null, {
        default: withCtx((slotProps) => [
          (openBlock(), createBlock(KeepAlive, null, [
            (openBlock(), createBlock(resolveDynamicComponent(slotProps.Component)))
          ], 1024))
        ]),
        _: 1
      });
    };
  }
});const __vite_glob_0_23 = "/index/img/board.png";const __vite_glob_0_27 = "/index/img/map.png";const __vite_glob_0_28 = "/index/img/page.png";const __vite_glob_0_29 = "/index/img/process.png";const __vite_glob_0_30 = "/index/img/%E5%BF%99%E7%A2%8C.png";const __vite_glob_0_31 = "/index/img/%E6%80%BB%E4%BD%93%E8%AE%BE%E8%AE%A1.png";const __vite_glob_0_32 = "/index/img/%E7%A9%BA%E9%97%B2.png";const __vite_glob_0_33 = "/index/img/%E9%94%99%E8%AF%AF.png";const TokenKey = "satoken";
const getToken = () => api.get(TokenKey);const service = axios.create({
  baseURL: "http://139.196.147.52:8080",
  timeout: 99999
});
service.interceptors.request.use(
  (config) => {
    if (getToken()) {
      config.headers["token"] = getToken();
    }
    return config;
  },
  (error) => {
    console.error(error);
    return Promise.reject(error);
  }
);
service.interceptors.response.use(
  (response) => {
    if (response.status !== 200) {
      ElMessage.error(response.data.message || "出现异常情况");
    }
    return response;
  },
  (error) => {
    if (error.message && error.message.includes("timeout")) {
      console.error("请求超时");
      return error.response;
    }
    if (error.response && error.response.status && error.response.status === 500) {
      ElMessage.error(error.response.data.message);
      console.log(error.response);
      return error.response;
    }
    console.log(error.response);
    return error.response;
  }
);const getMockDeviceInformation = () => service({
  url: "/mock-device-information",
  method: "get"
});const _hoisted_1$N = { style: { "padding": "20px", "display": "flex" } };
const _hoisted_2$E = /* @__PURE__ */ createBaseVNode("div", { style: { "margin-left": "30%", "margin-bottom": "20px" } }, "咖啡机器人", -1);
const _hoisted_3$z = ["src"];
const _hoisted_4$u = { style: { "line-height": "32px" } };
const _sfc_main$O = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    onMounted(() => {
      setInterval(function() {
        getDeviceInformation();
      }, 2e3);
    });
    const state = reactive({
      currentStatus: "-",
      currentOperation: "-"
    });
    const { currentStatus, currentOperation } = toRefs(state);
    const imageUrl = computed(() => {
      return new URL((/* #__PURE__ */ Object.assign({"../../../assets/images/board.png": __vite_glob_0_23,"../../../assets/images/map.png": __vite_glob_0_27,"../../../assets/images/page.png": __vite_glob_0_28,"../../../assets/images/process.png": __vite_glob_0_29,"../../../assets/images/忙碌.png": __vite_glob_0_30,"../../../assets/images/总体设计.png": __vite_glob_0_31,"../../../assets/images/空闲.png": __vite_glob_0_32,"../../../assets/images/错误.png": __vite_glob_0_33}))[`../../../assets/images/${currentStatus.value === "-" ? "空闲" : currentStatus.value}.png`], import.meta.url).href;
    });
    const getDeviceInformation = () => {
      getMockDeviceInformation().then((res) => {
        if (res.status === 200) {
          currentStatus.value = res.data.status;
          currentOperation.value = res.data.currentOperation === "" ? "无" : res.data.currentOperation;
        }
      });
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", _hoisted_1$N, [
        createBaseVNode("div", null, [
          _hoisted_2$E,
          createBaseVNode("img", {
            src: unref(imageUrl),
            alt: "device Image"
          }, null, 8, _hoisted_3$z)
        ]),
        createBaseVNode("div", _hoisted_4$u, [
          createBaseVNode("div", null, "当前状态：" + toDisplayString(unref(currentStatus)), 1),
          createBaseVNode("div", null, "当前操作：" + toDisplayString(unref(currentOperation)), 1)
        ])
      ]);
    };
  }
});const executeProcess = (processId, saveConfig) => service({
  url: `/execute-process?processId=${processId}`,
  method: "post",
  data: saveConfig
});const _hoisted_1$M = { style: { "padding": "20px" } };
const _hoisted_2$D = { style: { "margin-top": "20px" } };
const _hoisted_3$y = { style: { "width": "50%" } };
const _sfc_main$N = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      processName: "",
      processId: "",
      applicationName: "",
      scenarioName: "",
      executing: false,
      configIds: [],
      config: {},
      saveConfig: {},
      rules: {}
    });
    const { processId, processName, executing, configIds, config, saveConfig, applicationName, scenarioName, rules } = toRefs(state);
    const router = useRouter();
    onActivated(() => {
      if (typeof router.currentRoute.value.query.processId === "string") {
        processId.value = router.currentRoute.value.query.processId || "";
      }
      if (typeof router.currentRoute.value.query.processName === "string") {
        processName.value = router.currentRoute.value.query.processName || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    onMounted(() => {
      fetchConfig();
    });
    const formRefs = ref({});
    const fetchConfig = () => {
      configIds.value = ["makeCoffee"];
      config.value = {
        makeCoffee: [{
          code: "coffeeType",
          name: "咖啡类型",
          type: "Enum",
          optional: ["摩卡", "美式"],
          value: ""
        }]
      };
      configIds.value.forEach((configId) => {
        rules.value[configId] = {};
        config.value[configId].forEach((configItem) => {
          rules.value[configId][configItem.code] = [
            { required: true, message: `${configItem.name} is required`, trigger: "blur" }
          ];
        });
      });
    };
    const fetchExecuteProcess = () => {
      nextTick(() => {
        let allValid = true;
        configIds.value.forEach((configId) => {
          saveConfig.value[configId] = {};
          config.value[configId].forEach((configItem) => {
            saveConfig.value[configId][configItem.code] = configItem.value;
            if (configItem.value === "" || configItem.value === void 0) {
              allValid = false;
            }
          });
        });
        if (allValid) {
          executing.value = true;
          executeProcess(processId.value, saveConfig.value).then((res) => {
            if (res.status === 200) ;
            executing.value = false;
          });
        } else {
          ElMessage.warning("请进行相关配置");
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_breadcrumb_item = ElBreadcrumbItem;
      const _component_el_breadcrumb = ElBreadcrumb;
      const _component_el_input = ElInput;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      const _component_el_button = ElButton;
      const _directive_loading = vLoading;
      return openBlock(), createElementBlock("div", null, [
        createBaseVNode("div", _hoisted_1$M, [
          createBaseVNode("div", null, toDisplayString(unref(scenarioName)) + "-" + toDisplayString(unref(applicationName)) + "应用-" + toDisplayString(unref(processName)) + "功能", 1),
          createBaseVNode("div", _hoisted_2$D, [
            createVNode(_component_el_breadcrumb, { "separator-icon": unref(arrow_right_default) }, {
              default: withCtx(() => [
                createVNode(_component_el_breadcrumb_item, null, {
                  default: withCtx(() => [
                    createTextVNode("咖啡自助服务")
                  ]),
                  _: 1
                }),
                createVNode(_component_el_breadcrumb_item, null, {
                  default: withCtx(() => [
                    createTextVNode("点咖啡")
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }, 8, ["separator-icon"])
          ]),
          (openBlock(true), createElementBlock(Fragment, null, renderList(unref(configIds), (configId) => {
            return openBlock(), createElementBlock("div", {
              key: configId,
              style: { "margin-top": "20px" }
            }, [
              createVNode(_component_el_form, {
                model: unref(config)[configId],
                rules: unref(rules)[configId],
                ref_for: true,
                ref: (el) => unref(formRefs)[configId] = el
              }, {
                default: withCtx(() => [
                  (openBlock(true), createElementBlock(Fragment, null, renderList(unref(config)[configId], (configItem) => {
                    return openBlock(), createBlock(_component_el_form_item, {
                      label: configItem.name,
                      key: configItem.code,
                      required: "",
                      prop: configItem.code
                    }, {
                      default: withCtx(() => [
                        createBaseVNode("div", _hoisted_3$y, [
                          configItem.type === "String" ? (openBlock(), createBlock(_component_el_input, {
                            key: 0,
                            modelValue: configItem.value,
                            "onUpdate:modelValue": ($event) => configItem.value = $event
                          }, null, 8, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true),
                          configItem.type === "Number" ? (openBlock(), createBlock(_component_el_input, {
                            key: 1,
                            modelValue: configItem.value,
                            "onUpdate:modelValue": ($event) => configItem.value = $event,
                            type: "number"
                          }, null, 8, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true),
                          configItem.type === "Enum" ? (openBlock(), createBlock(_component_el_select, {
                            key: 2,
                            modelValue: configItem.value,
                            "onUpdate:modelValue": ($event) => configItem.value = $event
                          }, {
                            default: withCtx(() => [
                              (openBlock(true), createElementBlock(Fragment, null, renderList(configItem.optional, (option) => {
                                return openBlock(), createBlock(_component_el_option, {
                                  key: option,
                                  label: option,
                                  value: option
                                }, null, 8, ["label", "value"]);
                              }), 128))
                            ]),
                            _: 2
                          }, 1032, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true)
                        ])
                      ]),
                      _: 2
                    }, 1032, ["label", "prop"]);
                  }), 128))
                ]),
                _: 2
              }, 1032, ["model", "rules"])
            ]);
          }), 128)),
          createBaseVNode("div", null, [
            withDirectives((openBlock(), createBlock(_component_el_button, {
              onClick: fetchExecuteProcess,
              type: "primary",
              style: { "margin-top": "20px" }
            }, {
              default: withCtx(() => [
                createTextVNode("下单")
              ]),
              _: 1
            })), [
              [_directive_loading, unref(executing)]
            ])
          ])
        ])
      ]);
    };
  }
});const _hoisted_1$L = { style: { "padding": "20px" } };
const _hoisted_2$C = { style: { "display": "flex", "flex-direction": "row", "align-items": "center", "margin-top": "20px" } };
const _hoisted_3$x = /* @__PURE__ */ createBaseVNode("div", { style: { "border": "1px solid black", "padding": "10px", "margin-right": "10px", "margin-left": "10px" } }, "开始", -1);
const _hoisted_4$t = /* @__PURE__ */ createBaseVNode("div", null, "--->", -1);
const _hoisted_5$n = /* @__PURE__ */ createBaseVNode("div", null, "--->", -1);
const _hoisted_6$j = /* @__PURE__ */ createBaseVNode("div", { style: { "border": "1px solid black", "padding": "10px", "margin-left": "10px" } }, "检查", -1);
const _hoisted_7$h = { style: { "width": "50%" } };
const _hoisted_8$c = {
  slot: "footer",
  class: "dialog-footer"
};
const _sfc_main$M = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      processName: "",
      processId: "",
      applicationName: "",
      scenarioName: "",
      executing: false,
      configId: "",
      configTitle: "",
      configVisible: false,
      config: {},
      saveConfig: {}
    });
    const { processId, processName, executing, configId, configTitle, configVisible, config, saveConfig, applicationName, scenarioName } = toRefs(state);
    const router = useRouter();
    onActivated(() => {
      if (typeof router.currentRoute.value.query.processId === "string") {
        processId.value = router.currentRoute.value.query.processId || "";
      }
      if (typeof router.currentRoute.value.query.processName === "string") {
        processName.value = router.currentRoute.value.query.processName || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    onMounted(() => {
      fetchConfig();
    });
    const fetchConfig = () => {
      config.value = {
        makeCoffee: [{
          code: "coffeeType",
          name: "咖啡类型",
          type: "Enum",
          optional: ["摩卡", "美式"],
          value: ""
        }]
      };
    };
    const openConfig = async (actionId, actionName) => {
      configId.value = actionId;
      configTitle.value = actionName;
      configVisible.value = true;
    };
    const handleSaveConfig = () => {
      configVisible.value = false;
      saveConfig.value[configId.value] = {};
      config.value[configId.value].forEach((configItem) => {
        saveConfig.value[configId.value][configItem.code] = configItem.value;
      });
    };
    const fetchExecuteProcess = () => {
      let configComplete = Object.keys(config.value).every((key) => Object.keys(saveConfig.value).includes(key));
      if (configComplete) {
        executing.value = true;
        executeProcess(processId.value, saveConfig.value).then((res) => {
          if (res.status === 200) ;
          executing.value = false;
        });
      } else {
        ElMessage.warning("请进行相关配置");
      }
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      const _component_el_dialog = ElDialog;
      const _directive_loading = vLoading;
      return openBlock(), createElementBlock("div", null, [
        createBaseVNode("div", _hoisted_1$L, [
          createBaseVNode("div", null, toDisplayString(unref(scenarioName)) + "-" + toDisplayString(unref(applicationName)) + "应用-" + toDisplayString(unref(processName)) + "功能", 1),
          createBaseVNode("div", _hoisted_2$C, [
            _hoisted_3$x,
            _hoisted_4$t,
            createBaseVNode("div", {
              style: { "border": "1px solid black", "padding": "10px", "margin-right": "10px", "margin-left": "10px", "cursor": "pointer" },
              onClick: _cache[0] || (_cache[0] = ($event) => openConfig("makeCoffee", "做咖啡"))
            }, "做咖啡"),
            _hoisted_5$n,
            _hoisted_6$j
          ]),
          createBaseVNode("div", null, [
            withDirectives((openBlock(), createBlock(_component_el_button, {
              onClick: fetchExecuteProcess,
              type: "primary",
              style: { "margin-top": "20px" }
            }, {
              default: withCtx(() => [
                createTextVNode("执行")
              ]),
              _: 1
            })), [
              [_directive_loading, unref(executing)]
            ])
          ])
        ]),
        createVNode(_component_el_dialog, {
          title: unref(configTitle),
          modelValue: unref(configVisible),
          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => isRef(configVisible) ? configVisible.value = $event : null)
        }, {
          default: withCtx(() => [
            createVNode(_component_el_form, {
              model: unref(config)[unref(configId)]
            }, {
              default: withCtx(() => [
                (openBlock(true), createElementBlock(Fragment, null, renderList(unref(config)[unref(configId)], (configItem) => {
                  return openBlock(), createBlock(_component_el_form_item, {
                    label: configItem.name,
                    key: configItem.code
                  }, {
                    default: withCtx(() => [
                      createBaseVNode("div", _hoisted_7$h, [
                        configItem.type === "String" ? (openBlock(), createBlock(_component_el_input, {
                          key: 0,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event
                        }, null, 8, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true),
                        configItem.type === "Number" ? (openBlock(), createBlock(_component_el_input, {
                          key: 1,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event,
                          type: "number"
                        }, null, 8, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true),
                        configItem.type === "Enum" ? (openBlock(), createBlock(_component_el_select, {
                          key: 2,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event
                        }, {
                          default: withCtx(() => [
                            (openBlock(true), createElementBlock(Fragment, null, renderList(configItem.option, (option) => {
                              return openBlock(), createBlock(_component_el_option, {
                                key: option,
                                label: option,
                                value: option
                              }, null, 8, ["label", "value"]);
                            }), 128))
                          ]),
                          _: 2
                        }, 1032, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true)
                      ])
                    ]),
                    _: 2
                  }, 1032, ["label"]);
                }), 128))
              ]),
              _: 1
            }, 8, ["model"]),
            createBaseVNode("div", _hoisted_8$c, [
              createVNode(_component_el_button, {
                onClick: _cache[1] || (_cache[1] = ($event) => configVisible.value = false)
              }, {
                default: withCtx(() => [
                  createTextVNode("取消")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, {
                type: "primary",
                onClick: handleSaveConfig
              }, {
                default: withCtx(() => [
                  createTextVNode("保存")
                ]),
                _: 1
              })
            ])
          ]),
          _: 1
        }, 8, ["title", "modelValue"])
      ]);
    };
  }
});const _hoisted_1$K = { style: { "padding": "20px" } };
const _hoisted_2$B = { style: { "margin-top": "20px" } };
const _sfc_main$L = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      tableData: [],
      applicationName: "",
      scenarioName: ""
    });
    const { tableData, applicationName, scenarioName } = toRefs(state);
    const router = useRouter();
    onActivated(() => {
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    onMounted(() => {
      fetchData();
    });
    const fetchData = () => {
      tableData.value = [
        {
          processId: "ConferenceService",
          processName: "会议服务"
        }
      ];
    };
    const goToDetail = (processId, processName) => {
      router.push({ path: "/demo/page", query: { processId, processName, applicationName: applicationName.value, scenarioName: scenarioName.value } });
    };
    return (_ctx, _cache) => {
      const _component_el_table_column = ElTableColumn;
      const _component_el_button = ElButton;
      const _component_el_table = ElTable;
      return openBlock(), createElementBlock("div", _hoisted_1$K, [
        createBaseVNode("div", null, toDisplayString(unref(scenarioName)) + "-" + toDisplayString(unref(applicationName)) + "应用-功能列表", 1),
        createBaseVNode("div", _hoisted_2$B, [
          createVNode(_component_el_table, {
            data: unref(tableData),
            stripe: "",
            style: { "width": "60%" },
            border: ""
          }, {
            default: withCtx(() => [
              createVNode(_component_el_table_column, {
                prop: "processId",
                label: "功能代码"
              }),
              createVNode(_component_el_table_column, {
                prop: "processName",
                label: "功能名"
              }),
              createVNode(_component_el_table_column, {
                label: "操作",
                width: "150"
              }, {
                default: withCtx((scope) => [
                  createVNode(_component_el_button, {
                    type: "primary",
                    size: "small",
                    onClick: ($event) => goToDetail(scope.row.processId, scope.row.processName)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("打开")
                    ]),
                    _: 2
                  }, 1032, ["onClick"])
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["data"])
        ])
      ]);
    };
  }
});const _hoisted_1$J = { style: { "padding": "20px" } };
const _hoisted_2$A = { style: { "margin-top": "20px" } };
const _sfc_main$K = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      tableData: [],
      scenarioName: ""
    });
    const { tableData, scenarioName } = toRefs(state);
    const router = useRouter();
    onActivated(() => {
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    onMounted(() => {
      fetchData();
    });
    const fetchData = () => {
      tableData.value = [
        {
          applicationId: "GuestReception",
          applicationName: "来访接待"
        }
      ];
    };
    const goToDetail = (applicationId, applicationName) => {
      router.push({ path: "/demo/application/detail", query: { applicationId, applicationName, scenarioName: scenarioName.value } });
    };
    return (_ctx, _cache) => {
      const _component_el_table_column = ElTableColumn;
      const _component_el_button = ElButton;
      const _component_el_table = ElTable;
      return openBlock(), createElementBlock("div", _hoisted_1$J, [
        createBaseVNode("div", null, toDisplayString(unref(scenarioName)) + "-应用列表", 1),
        createBaseVNode("div", _hoisted_2$A, [
          createVNode(_component_el_table, {
            data: unref(tableData),
            stripe: "",
            style: { "width": "60%" },
            border: ""
          }, {
            default: withCtx(() => [
              createVNode(_component_el_table_column, {
                prop: "applicationId",
                label: "应用代码"
              }),
              createVNode(_component_el_table_column, {
                prop: "applicationName",
                label: "应用名"
              }),
              createVNode(_component_el_table_column, {
                label: "操作",
                width: "150"
              }, {
                default: withCtx((scope) => [
                  createVNode(_component_el_button, {
                    type: "primary",
                    size: "small",
                    onClick: ($event) => goToDetail(scope.row.applicationId, scope.row.applicationName)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("详情")
                    ]),
                    _: 2
                  }, 1032, ["onClick"]),
                  createVNode(_component_el_button, {
                    type: "success",
                    size: "small"
                  }, {
                    default: withCtx(() => [
                      createTextVNode("扫描")
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["data"])
        ])
      ]);
    };
  }
});const _hoisted_1$I = { style: { "padding": "20px" } };
const _hoisted_2$z = { style: { "margin-top": "20px", "width": "60%" } };
const _hoisted_3$w = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_4$s = /* @__PURE__ */ createBaseVNode("div", null, "设备列表", -1);
const _sfc_main$J = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      tableData: [],
      scenarioName: ""
    });
    const { tableData, scenarioName } = toRefs(state);
    const router = useRouter();
    onActivated(() => {
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    onMounted(() => {
      fetchData();
    });
    const fetchData = () => {
      tableData.value = [
        {
          deviceId: "deviceId",
          deviceName: "咖啡机器人A",
          deviceType: "咖啡机器人",
          deviceService: "AService",
          uri: "http://xx.xx.xx.xx"
        }
      ];
    };
    const goToDetail = (deviceId) => {
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_table_column = ElTableColumn;
      const _component_el_table = ElTable;
      return openBlock(), createElementBlock("div", _hoisted_1$I, [
        createBaseVNode("div", null, toDisplayString(unref(scenarioName)) + "-资源列表", 1),
        createBaseVNode("div", _hoisted_2$z, [
          createBaseVNode("div", _hoisted_3$w, [
            _hoisted_4$s,
            createVNode(_component_el_button, { type: "primary" }, {
              default: withCtx(() => [
                createTextVNode("添加设备")
              ]),
              _: 1
            })
          ]),
          createVNode(_component_el_table, {
            data: unref(tableData),
            stripe: "",
            style: { "margin-top": "10px" },
            border: ""
          }, {
            default: withCtx(() => [
              createVNode(_component_el_table_column, {
                prop: "deviceName",
                label: "设备名"
              }),
              createVNode(_component_el_table_column, {
                prop: "deviceType",
                label: "设备类型"
              }),
              createVNode(_component_el_table_column, {
                prop: "deviceService",
                label: "设备厂商"
              }),
              createVNode(_component_el_table_column, {
                prop: "uri",
                label: "uri"
              }),
              createVNode(_component_el_table_column, {
                label: "操作",
                width: "150"
              }, {
                default: withCtx((scope) => [
                  createVNode(_component_el_button, {
                    type: "primary",
                    size: "small",
                    disabled: "",
                    onClick: ($event) => goToDetail(scope.row.deviceId)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("查看详情")
                    ]),
                    _: 2
                  }, 1032, ["onClick"])
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["data"])
        ])
      ]);
    };
  }
});const _hoisted_1$H = { style: { "padding": "20px" } };
const _hoisted_2$y = /* @__PURE__ */ createBaseVNode("div", null, "场景列表", -1);
const _hoisted_3$v = { style: { "margin-top": "20px" } };
const _sfc_main$I = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      tableData: []
    });
    const { tableData } = toRefs(state);
    const router = useRouter();
    onMounted(() => {
      fetchData();
    });
    const fetchData = () => {
      tableData.value = [
        {
          scenarioId: "BuildingA",
          scenarioName: "交叉二号楼"
        }
      ];
    };
    const goToApplicationList = (scenarioId, scenarioName) => {
      router.push({ path: "/demo/applicationList", query: { scenarioId, scenarioName } });
    };
    const goToResourceList = (scenarioId, scenarioName) => {
      router.push({ path: "/demo/resource", query: { scenarioId, scenarioName } });
    };
    return (_ctx, _cache) => {
      const _component_el_table_column = ElTableColumn;
      const _component_el_button = ElButton;
      const _component_el_table = ElTable;
      return openBlock(), createElementBlock("div", _hoisted_1$H, [
        _hoisted_2$y,
        createBaseVNode("div", _hoisted_3$v, [
          createVNode(_component_el_table, {
            data: unref(tableData),
            stripe: "",
            style: { "width": "60%" },
            border: ""
          }, {
            default: withCtx(() => [
              createVNode(_component_el_table_column, {
                prop: "scenarioId",
                label: "场景代码"
              }),
              createVNode(_component_el_table_column, {
                prop: "scenarioName",
                label: "场景名"
              }),
              createVNode(_component_el_table_column, {
                label: "操作",
                width: "300"
              }, {
                default: withCtx((scope) => [
                  createVNode(_component_el_button, {
                    type: "primary",
                    size: "small",
                    onClick: ($event) => goToResourceList(scope.row.scenarioId, scope.row.scenarioName)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("查看资源")
                    ]),
                    _: 2
                  }, 1032, ["onClick"]),
                  createVNode(_component_el_button, {
                    type: "primary",
                    size: "small",
                    onClick: ($event) => goToApplicationList(scope.row.scenarioId, scope.row.scenarioName)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("查看应用")
                    ]),
                    _: 2
                  }, 1032, ["onClick"])
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["data"])
        ])
      ]);
    };
  }
});const useUserStore = defineStore("user", {
  /**组件的data， 用于存储全局状态, 并实现初始化
  * 1.必须是函数，这样是为了在服务端渲染的时候避免交叉请求导致的数据状态污染
  * 2.必须是箭头函数，这是为了更好的TS类型推到
  * 返回值：一个函数，调用得到容器
  */
  state: () => ({
    username: "",
    isAuthenticated: false,
    roles: []
  }),
  //类似于组件的computed ，用来封装计算属性，有缓存的功能
  //函数接收一个可选参数state 状态对象
  //如果在 getters中使用了this ,则必须手动指定返回值的类型，后者类型推推导不出来
  getters: {
    getUsername: (state) => state.username,
    getRoles: (state) => state.roles,
    getIsAuthenticated: (state) => state.isAuthenticated
  },
  //类似于组件的 methods，封装业务逻辑， 修改 state
  //不能使用箭头函数定义action，因为箭头函数绑定外部 this
  actions: {
    login(user) {
      console.log(user);
      this.username = user.username;
      this.isAuthenticated = true;
      this.roles = user.roles;
    },
    logout() {
      this.username = "";
      this.isAuthenticated = false;
      this.roles = [];
    }
  },
  // pinia数据持久化，解决刷新后数据丢失问题
  persist: true
});const _withScopeId$a = (n) => (pushScopeId("data-v-3583889e"), n = n(), popScopeId(), n);
const _hoisted_1$G = {
  key: 0,
  style: { "margin-top": "10px" }
};
const _hoisted_2$x = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "sub-title" }, "系统业务", -1));
const _hoisted_3$u = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "应用管理", -1));
const _hoisted_4$r = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "用户管理", -1));
const _hoisted_5$m = {
  key: 1,
  style: { "margin-top": "10px" }
};
const _hoisted_6$i = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "sub-title" }, "工作台", -1));
const _hoisted_7$g = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "模板库", -1));
const _hoisted_8$b = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "我的空间", -1));
const _hoisted_9$9 = {
  key: 2,
  style: { "margin-top": "10px" }
};
const _hoisted_10$5 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "sub-title" }, "业务区", -1));
const _hoisted_11$3 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "业务管理", -1));
const _hoisted_12$2 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "我的空间", -1));
const _hoisted_13$1 = {
  key: 3,
  style: { "margin-top": "10px" }
};
const _hoisted_14$1 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "为你推荐", -1));
const _hoisted_15 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "我的应用", -1));
const _hoisted_16 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "sub-title" }, "设置", -1));
const _hoisted_17 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "个人资料", -1));
const _hoisted_18 = /* @__PURE__ */ _withScopeId$a(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "账号设置", -1));
const _sfc_main$H = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const userStore = useUserStore();
    const { roles } = storeToRefs(userStore);
    const state = reactive({
      selectedItem: "",
      items: [
        // 管理员
        {
          index: "auth",
          name: "权限配置",
          route: "/admin/auth"
        },
        {
          index: "publish-setting",
          name: "应用发布",
          route: "/admin/publish-setting"
        },
        {
          index: "userManage",
          name: "用户管理",
          route: "/admin/userManage"
        },
        // 开发人员
        {
          index: "template",
          name: "模板库",
          route: "/developer/template"
        },
        {
          index: "workspace",
          name: "我的空间",
          route: "/developer/workspace"
        },
        //业务人员
        {
          index: "businessManage",
          name: "业务管理",
          route: "/business/businessManage"
        },
        {
          index: "businessWorkspace",
          name: "业务空间",
          route: "/business/businessWorkspace"
        },
        // 普通用户
        {
          index: "recommendation",
          name: "为你推荐",
          route: "/user/recommendation"
        },
        {
          index: "application",
          name: "我的应用",
          route: "/user/my-application"
        },
        {
          index: "profile",
          name: "个人资料",
          route: "/user/my-profile"
        },
        {
          index: "setting",
          name: "账号设置",
          route: "/user/my-setting"
        }
      ]
    });
    const { selectedItem, items } = toRefs(state);
    onMounted(async () => {
    });
    watchEffect(() => {
      const path = router.currentRoute.value.path;
      const matchingItemData = items.value.find((item) => item.route === path);
      if (matchingItemData) {
        selectedItem.value = matchingItemData.index;
      } else {
        selectedItem.value = "";
      }
    });
    const handleMenuSelect = (key, _keyPath) => {
      selectedItem.value = key;
      const selectedItemData = items.value.find((item) => item.index === key);
      if (selectedItemData) {
        router.push(selectedItemData.route);
      }
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_menu_item = ElMenuItem;
      const _component_el_sub_menu = ElSubMenu;
      const _component_el_menu = ElMenu;
      return openBlock(), createBlock(_component_el_menu, {
        "default-active": unref(selectedItem),
        class: "nav-menu",
        onSelect: handleMenuSelect
      }, {
        default: withCtx(() => [
          unref(roles).includes("admin") ? (openBlock(), createElementBlock("div", _hoisted_1$G, [
            _hoisted_2$x,
            createVNode(_component_el_sub_menu, {
              index: "0",
              class: "nav-item"
            }, {
              title: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(monitor_default))
                  ]),
                  _: 1
                }),
                _hoisted_3$u
              ]),
              default: withCtx(() => [
                createVNode(_component_el_menu_item, {
                  index: "auth",
                  class: "sub-menu-item"
                }, {
                  default: withCtx(() => [
                    createTextVNode("权限配置")
                  ]),
                  _: 1
                }),
                createVNode(_component_el_menu_item, {
                  index: "publish-setting",
                  class: "sub-menu-item"
                }, {
                  default: withCtx(() => [
                    createTextVNode("应用发布")
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }),
            createVNode(_component_el_menu_item, {
              index: "userManage",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(user_default))
                  ]),
                  _: 1
                }),
                _hoisted_4$r
              ]),
              _: 1
            })
          ])) : createCommentVNode("", true),
          unref(roles).includes("developer") || unref(roles).includes("admin") ? (openBlock(), createElementBlock("div", _hoisted_5$m, [
            _hoisted_6$i,
            createVNode(_component_el_menu_item, {
              index: "template",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(compass_default))
                  ]),
                  _: 1
                }),
                _hoisted_7$g
              ]),
              _: 1
            }),
            createVNode(_component_el_menu_item, {
              index: "workspace",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(data_board_default))
                  ]),
                  _: 1
                }),
                _hoisted_8$b
              ]),
              _: 1
            })
          ])) : createCommentVNode("", true),
          unref(roles).includes("business") ? (openBlock(), createElementBlock("div", _hoisted_9$9, [
            _hoisted_10$5,
            createVNode(_component_el_menu_item, {
              index: "businessManage",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(compass_default))
                  ]),
                  _: 1
                }),
                _hoisted_11$3
              ]),
              _: 1
            }),
            createVNode(_component_el_menu_item, {
              index: "workspace",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(data_board_default))
                  ]),
                  _: 1
                }),
                _hoisted_12$2
              ]),
              _: 1
            })
          ])) : createCommentVNode("", true),
          unref(roles).includes("user") ? (openBlock(), createElementBlock("div", _hoisted_13$1, [
            createVNode(_component_el_menu_item, {
              index: "recommendation",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(compass_default))
                  ]),
                  _: 1
                }),
                _hoisted_14$1
              ]),
              _: 1
            }),
            createVNode(_component_el_menu_item, {
              index: "application",
              class: "nav-item"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(house_default))
                  ]),
                  _: 1
                }),
                _hoisted_15
              ]),
              _: 1
            }),
            createBaseVNode("div", null, [
              _hoisted_16,
              createVNode(_component_el_menu_item, {
                index: "profile",
                class: "nav-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_icon, null, {
                    default: withCtx(() => [
                      createVNode(unref(user_default))
                    ]),
                    _: 1
                  }),
                  _hoisted_17
                ]),
                _: 1
              }),
              createVNode(_component_el_menu_item, {
                index: "setting",
                class: "nav-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_icon, null, {
                    default: withCtx(() => [
                      createVNode(unref(setting_default))
                    ]),
                    _: 1
                  }),
                  _hoisted_18
                ]),
                _: 1
              })
            ])
          ])) : createCommentVNode("", true)
        ]),
        _: 1
      }, 8, ["default-active"]);
    };
  }
});/* unplugin-vue-components disabled */const _export_sfc = (sfc, props) => {
  const target = sfc.__vccOpts || sfc;
  for (const [key, val] of props) {
    target[key] = val;
  }
  return target;
};const AsideBar = /* @__PURE__ */ _export_sfc(_sfc_main$H, [["__scopeId", "data-v-3583889e"]]);const logo = "/index/img/logo.png";const _hoisted_1$F = { class: "header" };
const _hoisted_2$w = ["src"];
const _hoisted_3$t = /* @__PURE__ */ createBaseVNode("div", { style: { "line-height": "32px", "padding-left": "10px" } }, "面向场景计算的低代码平台", -1);
const _hoisted_4$q = { class: "header-right" };
const _hoisted_5$l = { style: { "margin-right": "20px" } };
const _sfc_main$G = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const router = useRouter();
    const userStore = useUserStore();
    const { username, isAuthenticated, roles } = storeToRefs(userStore);
    const handleCommand = (command) => {
      userStore.login({
        username: "test",
        roles: [command]
      });
      router.push({ path: "/" });
    };
    watch([isAuthenticated, username, roles], () => {
      console.log("isAuthenticated:", isAuthenticated.value);
      console.log("username:", username.value);
      console.log("roles", roles.value);
    });
    const handleDropdownItemClick = (item) => {
      if (item === "developer") {
        router.push({ path: "/developer/setting" });
      }
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      const _component_el_dropdown_item = ElDropdownItem;
      const _component_el_dropdown_menu = ElDropdownMenu;
      const _component_el_dropdown = ElDropdown;
      return openBlock(), createElementBlock("div", _hoisted_1$F, [
        createBaseVNode("div", {
          class: "header-title",
          style: { "cursor": "pointer" },
          onClick: _cache[0] || (_cache[0] = ($event) => unref(router).push({ path: "/" }))
        }, [
          createBaseVNode("img", {
            width: "30",
            height: "30",
            title: "低代码",
            src: unref(logo)
          }, null, 8, _hoisted_2$w),
          _hoisted_3$t
        ]),
        createBaseVNode("div", _hoisted_4$q, [
          createBaseVNode("div", _hoisted_5$l, [
            createVNode(_component_el_dropdown, { onCommand: handleDropdownItemClick }, {
              dropdown: withCtx(() => [
                createVNode(_component_el_dropdown_menu, null, {
                  default: withCtx(() => [
                    createVNode(_component_el_dropdown_item, {
                      key: "systemConfig",
                      command: "system"
                    }, {
                      default: withCtx(() => [
                        createTextVNode("系统设置")
                      ]),
                      _: 1
                    }),
                    unref(roles).includes("developer") || unref(roles).includes("admin") ? (openBlock(), createBlock(_component_el_dropdown_item, {
                      key: "developerConfig",
                      command: "developer"
                    }, {
                      default: withCtx(() => [
                        createTextVNode("开发者设置")
                      ]),
                      _: 1
                    })) : createCommentVNode("", true)
                  ]),
                  _: 1
                })
              ]),
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  link: "",
                  style: { "display": "flex", "color": "white" }
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_icon, {
                      style: { "margin-right": "2px", "margin-top": "1px" },
                      size: "20",
                      color: "white"
                    }, {
                      default: withCtx(() => [
                        createVNode(unref(setting_default))
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          createVNode(_component_el_dropdown, { onCommand: handleCommand }, {
            dropdown: withCtx(() => [
              createVNode(_component_el_dropdown_menu, null, {
                default: withCtx(() => [
                  createVNode(_component_el_dropdown_item, { command: "admin" }, {
                    default: withCtx(() => [
                      createTextVNode("系统管理员")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_dropdown_item, { command: "developer" }, {
                    default: withCtx(() => [
                      createTextVNode("场景配置员")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_dropdown_item, { command: "business" }, {
                    default: withCtx(() => [
                      createTextVNode("业务人员")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_dropdown_item, { command: "user" }, {
                    default: withCtx(() => [
                      createTextVNode("普通用户")
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            default: withCtx(() => [
              createVNode(_component_el_button, {
                link: "",
                style: { "display": "flex", "color": "white", "font-size": "16px" }
              }, {
                default: withCtx(() => [
                  createBaseVNode("div", null, toDisplayString(unref(isAuthenticated) ? unref(username) : "登录/注册"), 1)
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled */const _hoisted_1$E = { class: "common-layout" };
const _sfc_main$F = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const userStore = useUserStore();
    const { isAuthenticated } = storeToRefs(userStore);
    return (_ctx, _cache) => {
      const _component_el_header = ElHeader;
      const _component_el_aside = ElAside;
      const _component_RouterView = resolveComponent("RouterView");
      const _component_el_main = ElMain;
      const _component_el_container = ElContainer;
      return openBlock(), createElementBlock("div", _hoisted_1$E, [
        createVNode(_component_el_container, { style: { "width": "100%", "height": "100%" } }, {
          default: withCtx(() => [
            createVNode(_component_el_header, { style: { "padding": "0" } }, {
              default: withCtx(() => [
                createVNode(_sfc_main$G)
              ]),
              _: 1
            }),
            createVNode(_component_el_container, null, {
              default: withCtx(() => [
                unref(isAuthenticated) ? (openBlock(), createBlock(_component_el_aside, {
                  key: 0,
                  width: "200px"
                }, {
                  default: withCtx(() => [
                    createVNode(AsideBar)
                  ]),
                  _: 1
                })) : createCommentVNode("", true),
                createVNode(_component_el_main, { style: { "padding": "30px 50px 50px" } }, {
                  default: withCtx(() => [
                    createVNode(_component_RouterView, null, {
                      default: withCtx((slotProps) => [
                        (openBlock(), createBlock(KeepAlive, null, [
                          (openBlock(), createBlock(resolveDynamicComponent(slotProps.Component)))
                        ], 1024))
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ]);
    };
  }
});/* unplugin-vue-components disabled */const MainLayout = /* @__PURE__ */ _export_sfc(_sfc_main$F, [["__scopeId", "data-v-306120af"]]);/* unplugin-vue-components disabled */const _sfc_main$E = {};

const _hoisted_1$D = { style: {"width":"80%"} };

function _sfc_render$1(_ctx, _cache) {
  return (openBlock(), createElementBlock("div", _hoisted_1$D, " 首页 "))
}
const HomeView = /*#__PURE__*/_export_sfc(_sfc_main$E, [['render',_sfc_render$1]]);const _withScopeId$9 = (n) => (pushScopeId("data-v-6ee3216d"), n = n(), popScopeId(), n);
const _hoisted_1$C = { class: "file-item" };
const _hoisted_2$v = {
  key: 0,
  style: { "z-index": "3" }
};
const _hoisted_3$s = {
  key: 0,
  style: { "margin": "0 auto", "text-align": "center" }
};
const _hoisted_4$p = /* @__PURE__ */ _withScopeId$9(() => /* @__PURE__ */ createBaseVNode("span", { class: "publish-span" }, "已发布", -1));
const _hoisted_5$k = [
  _hoisted_4$p
];
const _hoisted_6$h = {
  key: 1,
  style: { "margin": "0 auto", "text-align": "center" }
};
const _hoisted_7$f = /* @__PURE__ */ _withScopeId$9(() => /* @__PURE__ */ createBaseVNode("span", { class: "not-publish-span" }, "待发布", -1));
const _hoisted_8$a = [
  _hoisted_7$f
];
const _hoisted_9$8 = { class: "file-name" };
const _sfc_main$D = /* @__PURE__ */ defineComponent({
  __name: "Card",
  props: {
    cardItem: Object,
    dropDownItems: {
      type: Array,
      default: []
    },
    canSelect: {
      type: Boolean,
      default: false
    },
    needPublish: {
      type: Boolean,
      default: false
    }
  },
  emits: ["update:isSelected", "commandClick", "itemClick"],
  setup(__props, { emit: __emit }) {
    const emit = __emit;
    const handleCheckboxChange = (value) => {
      emit("update:isSelected", value);
    };
    const state = reactive({
      isHovered: false
    });
    const { isHovered } = toRefs(state);
    const handleItemClick = () => {
      isHovered.value = false;
      emit("itemClick");
    };
    const handleDropdownItemClick = (item) => {
      emit("commandClick", item);
    };
    return (_ctx, _cache) => {
      const _component_el_checkbox = ElCheckbox;
      const _component_el_image = ElImage;
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      const _component_el_dropdown_item = ElDropdownItem;
      const _component_el_dropdown_menu = ElDropdownMenu;
      const _component_el_dropdown = ElDropdown;
      return openBlock(), createElementBlock("div", _hoisted_1$C, [
        createBaseVNode("div", {
          class: normalizeClass(["file-card-item", { "file-card-item-selected": __props.canSelect && __props.cardItem.isSelected }]),
          onMouseover: _cache[0] || (_cache[0] = ($event) => isHovered.value = true),
          onMouseleave: _cache[1] || (_cache[1] = ($event) => isHovered.value = false)
        }, [
          __props.canSelect ? (openBlock(), createElementBlock("div", _hoisted_2$v, [
            withDirectives(createVNode(_component_el_checkbox, mergeProps(__props.cardItem.isSelected, {
              onChange: handleCheckboxChange,
              class: "checkbox",
              size: "large"
            }), null, 16), [
              [vShow, unref(isHovered) || __props.cardItem.isSelected]
            ])
          ])) : createCommentVNode("", true),
          createBaseVNode("div", {
            style: { "z-index": "1", "margin": "15px" },
            onClick: handleItemClick
          }, [
            createVNode(_component_el_image, {
              src: __props.cardItem.imageUrl,
              class: "folder-icon"
            }, null, 8, ["src"]),
            Boolean(__props.needPublish) && Boolean(__props.cardItem.isPublish) ? (openBlock(), createElementBlock("div", _hoisted_3$s, _hoisted_5$k)) : createCommentVNode("", true),
            Boolean(__props.needPublish) && !Boolean(__props.cardItem.isPublish) ? (openBlock(), createElementBlock("div", _hoisted_6$h, _hoisted_8$a)) : createCommentVNode("", true)
          ]),
          __props.dropDownItems.length !== 0 ? (openBlock(), createBlock(_component_el_dropdown, {
            key: 1,
            trigger: "click",
            class: "more-options",
            onCommand: handleDropdownItemClick
          }, {
            dropdown: withCtx(() => [
              createVNode(_component_el_dropdown_menu, null, {
                default: withCtx(() => [
                  (openBlock(true), createElementBlock(Fragment, null, renderList(__props.dropDownItems, (item) => {
                    return openBlock(), createBlock(_component_el_dropdown_item, {
                      key: item.code,
                      command: item.code
                    }, {
                      default: withCtx(() => [
                        createTextVNode(toDisplayString(item.name), 1)
                      ]),
                      _: 2
                    }, 1032, ["command"]);
                  }), 128))
                ]),
                _: 1
              })
            ]),
            default: withCtx(() => [
              createBaseVNode("div", null, [
                unref(isHovered) ? (openBlock(), createBlock(_component_el_button, { key: 0 }, {
                  default: withCtx(() => [
                    createVNode(_component_el_icon, null, {
                      default: withCtx(() => [
                        createVNode(unref(more_default))
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })) : createCommentVNode("", true)
              ])
            ]),
            _: 1
          })) : createCommentVNode("", true)
        ], 34),
        createBaseVNode("div", _hoisted_9$8, toDisplayString(__props.cardItem.name), 1)
      ]);
    };
  }
});/* unplugin-vue-components disabled */const Card = /* @__PURE__ */ _export_sfc(_sfc_main$D, [["__scopeId", "data-v-6ee3216d"]]);const __vite_glob_0_0 = "/index/img/Conditioner.png";const __vite_glob_0_1 = "/index/img/CoffeeMaker.png";const __vite_glob_0_2 = "/index/img/Conditioner.png";const __vite_glob_0_3 = "/index/img/SmokeDetector.png";const __vite_glob_0_4 = "/index/img/analysis.png";const __vite_glob_0_5 = "/index/img/bar.svg";const __vite_glob_0_6 = "/index/img/bug.png";const __vite_glob_0_7 = "/index/img/button.png";const __vite_glob_0_8 = "/index/img/container.png";const __vite_glob_0_9 = "/index/img/decision.png";const __vite_glob_0_10 = "/index/img/device.png";const __vite_glob_0_11 = "/index/img/deviceType.png";const __vite_glob_0_12 = "/index/img/endNode.png";const __vite_glob_0_13 = "/index/img/guide.svg";const __vite_glob_0_14 = "/index/img/image.png";const __vite_glob_0_15 = "/index/img/modal.png";const __vite_glob_0_16 = "/index/img/parallel.png";const __vite_glob_0_17 = "/index/img/process-template.png";const __vite_glob_0_18 = "/index/img/startNode.png";const __vite_glob_0_19 = "/index/img/text.png";const __vite_glob_0_20 = "/index/img/xor.png";const __vite_glob_0_21 = "/index/img/xunhuan.png";const __vite_glob_0_22 = "/index/img/bar.svg";const __vite_glob_0_24 = "/index/img/floor.jpeg";const __vite_glob_0_25 = "/index/img/guide.svg";const __vite_glob_0_26 = "/index/img/header-image.jpg";const __vite_glob_0_34 = "/index/img/logo.png";const __vite_glob_0_35 = "/index/img/copy.png";const __vite_glob_0_36 = "/index/img/generate.png";const __vite_glob_0_37 = "/index/img/package.png";const __vite_glob_0_38 = "/index/css/style.css";const getAssetsFile = (url) => {
  return new URL((/* #__PURE__ */ Object.assign({"../assets/device/AirConditioner.png": __vite_glob_0_0,"../assets/device/CoffeeMaker.png": __vite_glob_0_1,"../assets/device/Conditioner.png": __vite_glob_0_2,"../assets/device/SmokeDetector.png": __vite_glob_0_3,"../assets/icon/analysis.png": __vite_glob_0_4,"../assets/icon/bar.svg": __vite_glob_0_5,"../assets/icon/bug.png": __vite_glob_0_6,"../assets/icon/button.png": __vite_glob_0_7,"../assets/icon/container.png": __vite_glob_0_8,"../assets/icon/decision.png": __vite_glob_0_9,"../assets/icon/device.png": __vite_glob_0_10,"../assets/icon/deviceType.png": __vite_glob_0_11,"../assets/icon/endNode.png": __vite_glob_0_12,"../assets/icon/guide.svg": __vite_glob_0_13,"../assets/icon/image.png": __vite_glob_0_14,"../assets/icon/modal.png": __vite_glob_0_15,"../assets/icon/parallel.png": __vite_glob_0_16,"../assets/icon/process-template.png": __vite_glob_0_17,"../assets/icon/startNode.png": __vite_glob_0_18,"../assets/icon/text.png": __vite_glob_0_19,"../assets/icon/xor.png": __vite_glob_0_20,"../assets/icon/xunhuan.png": __vite_glob_0_21,"../assets/images/bar.svg": __vite_glob_0_22,"../assets/images/board.png": __vite_glob_0_23,"../assets/images/floor.jpeg": __vite_glob_0_24,"../assets/images/guide.svg": __vite_glob_0_25,"../assets/images/header-image.jpg": __vite_glob_0_26,"../assets/images/map.png": __vite_glob_0_27,"../assets/images/page.png": __vite_glob_0_28,"../assets/images/process.png": __vite_glob_0_29,"../assets/images/忙碌.png": __vite_glob_0_30,"../assets/images/总体设计.png": __vite_glob_0_31,"../assets/images/空闲.png": __vite_glob_0_32,"../assets/images/错误.png": __vite_glob_0_33,"../assets/logo.png": __vite_glob_0_34,"../assets/progress/copy.png": __vite_glob_0_35,"../assets/progress/generate.png": __vite_glob_0_36,"../assets/progress/package.png": __vite_glob_0_37,"../assets/style.css": __vite_glob_0_38}))[`../assets/${url}`], import.meta.url).href;
};const _hoisted_1$B = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _sfc_main$C = /* @__PURE__ */ defineComponent({
  __name: "MyDomain",
  setup(__props) {
    const router = useRouter();
    onActivated(() => {
      domainList.value = [
        {
          code: "SmartBuilding",
          name: "智慧楼宇",
          imageUrl: getAssetsFile("logo.png"),
          isSelected: false
        },
        {
          code: "SmartMine",
          name: "智慧矿山",
          imageUrl: getAssetsFile("logo.png"),
          isSelected: false
        }
      ];
    });
    const state = reactive({
      domainList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        },
        {
          code: "edit",
          name: "编辑"
        }
      ]
    });
    const { domainList, dropDownItems } = toRefs(state);
    const updateIsSelected = (index, value) => {
      domainList.value[index].isSelected = value;
    };
    const handleCommand = (domain, command) => {
      console.log("Clicked item:", domain, command);
    };
    const handleClick = (domain) => {
      console.log(domain);
      router.push({ path: "/developer/domain/detail", query: { domainId: domain.code, domainName: domain.name } });
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", _hoisted_1$B, [
        (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainList), (domain, index) => {
          return openBlock(), createElementBlock("div", { key: index }, [
            createVNode(Card, {
              cardItem: domain,
              canSelect: "",
              dropDownItems: unref(dropDownItems),
              onCommandClick: ($event) => handleCommand(domain, $event),
              onItemClick: ($event) => handleClick(domain),
              "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
            }, null, 8, ["cardItem", "dropDownItems", "onCommandClick", "onItemClick", "onUpdate:isSelected"])
          ]);
        }), 128))
      ]);
    };
  }
});const getDomainJson = () => service({
  url: `/load-domain-json`,
  method: "get"
});
const getDomainComponent = (componentType) => service({
  url: `/load-domain-component-json?componentType=${componentType}`,
  method: "get"
});
const loadDoaminComponentData = (componentType) => service({
  url: `/load-domain-component-data?componentType=${componentType}`,
  method: "get"
});
const uploadDomainBindingData = (data, componentType, domainCode) => service({
  url: `/upload-domain-component-binding?componentType=${componentType}&domainCode=${domainCode}`,
  method: "post",
  data
});
const loadDomainBindingData = (componentType, domainCode) => service({
  url: `/load-domain-component-binding?componentType=${componentType}&domainCode=${domainCode}`,
  method: "get"
});var define_import_meta_env_default$9 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$A = { class: "component-container" };
const _hoisted_2$u = {
  class: "component-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_3$r = {
  key: 0,
  class: "component-content"
};
const _hoisted_4$o = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _hoisted_5$j = {
  style: { "margin-top": "30px" },
  class: "component-container"
};
const _hoisted_6$g = {
  class: "component-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_7$e = {
  key: 0,
  class: "component-content"
};
const _hoisted_8$9 = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _sfc_main$B = /* @__PURE__ */ defineComponent({
  __name: "MyComponent",
  setup(__props, { expose: __expose }) {
    const state = reactive({
      deviceData: [],
      businessData: [],
      deviceVisible: true,
      businessVisible: true
    });
    const { deviceData, businessData, deviceVisible, businessVisible } = toRefs(state);
    onMounted(async () => {
      if (define_import_meta_env_default$9.VITE_MODE === "mock") {
        deviceData.value = [
          {
            code: "CoffeeMaker",
            name: "咖啡机器人",
            imageUrl: getAssetsFile("device/CoffeeMaker.png"),
            isSelected: false
          },
          {
            code: "AirConditioner",
            name: "空调",
            imageUrl: getAssetsFile("device/AirConditioner.png"),
            isSelected: false
          },
          {
            code: "SmokeDetector",
            name: "烟感器",
            imageUrl: getAssetsFile("device/SmokeDetector.png"),
            isSelected: false
          }
        ];
      } else {
        businessData.value = [
          {
            code: "ConferenceService",
            name: "会议接待流程图",
            imageUrl: getAssetsFile("icon/process-template.png"),
            isSelected: false
          }
        ];
      }
    });
    const router = useRouter();
    const updateDeviceIsSelected = (index, value) => {
      deviceData.value[index].isSelected = value;
    };
    const handleDeviceClick = (device) => {
      router.push({ path: "/developer/device-component/detail", query: { deviceCode: device.code, deviceName: device.name } });
    };
    const updateBusinessIsSelected = (index, value) => {
      businessData.value[index].isSelected = value;
    };
    const handleBusinessClick = (device) => {
      console.log(device);
    };
    const getComponentData = async () => {
      await loadDoaminComponentData("Device").then((res) => {
        deviceData.value = [];
        if (res.status === 200) {
          const dataArray = res.data;
          dataArray.forEach(async (device) => {
            const newDevice = {
              code: device.deviceTypeCode,
              name: device.deviceTypeName,
              isSelected: false,
              isPublish: device.publish,
              imageUrl: getAssetsFile("device/" + device.deviceTypeCode + ".png")
            };
            deviceData.value.push(newDevice);
          });
        }
      });
    };
    __expose({
      getComponentData
    });
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$A, [
          createBaseVNode("div", _hoisted_2$u, [
            createBaseVNode("div", {
              onClick: _cache[0] || (_cache[0] = ($event) => deviceVisible.value = !unref(deviceVisible)),
              style: { "cursor": "pointer" }
            }, [
              createTextVNode(" 设备组件(" + toDisplayString(unref(deviceData).length) + ") ", 1),
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  unref(deviceVisible) ? (openBlock(), createBlock(unref(caret_bottom_default), { key: 0 })) : createCommentVNode("", true),
                  !unref(deviceVisible) ? (openBlock(), createBlock(unref(caret_top_default), { key: 1 })) : createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            createVNode(_component_el_button, {
              type: "primary",
              onClick: _cache[1] || (_cache[1] = ($event) => unref(router).push({ path: "/developer/device-component/add" }))
            }, {
              default: withCtx(() => [
                createTextVNode("新增设备组件")
              ]),
              _: 1
            })
          ]),
          unref(deviceVisible) ? (openBlock(), createElementBlock("div", _hoisted_3$r, [
            createBaseVNode("div", _hoisted_4$o, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(unref(deviceData), (device, index) => {
                return openBlock(), createElementBlock("div", { key: index }, [
                  createVNode(Card, {
                    cardItem: device,
                    canSelect: "",
                    needPublish: "",
                    onItemClick: ($event) => handleDeviceClick(device),
                    "onUpdate:isSelected": ($event) => updateDeviceIsSelected(index, $event)
                  }, null, 8, ["cardItem", "onItemClick", "onUpdate:isSelected"])
                ]);
              }), 128))
            ])
          ])) : createCommentVNode("", true)
        ]),
        createBaseVNode("div", _hoisted_5$j, [
          createBaseVNode("div", _hoisted_6$g, [
            createBaseVNode("div", {
              onClick: _cache[2] || (_cache[2] = ($event) => businessVisible.value = !unref(businessVisible)),
              style: { "cursor": "pointer" }
            }, [
              createTextVNode(" 业务模板(" + toDisplayString(unref(businessData).length) + ") ", 1),
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  unref(businessVisible) ? (openBlock(), createBlock(unref(caret_bottom_default), { key: 0 })) : createCommentVNode("", true),
                  !unref(businessVisible) ? (openBlock(), createBlock(unref(caret_top_default), { key: 1 })) : createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            createVNode(_component_el_button, { type: "primary" }, {
              default: withCtx(() => [
                createTextVNode("新增业务模板")
              ]),
              _: 1
            })
          ]),
          unref(businessVisible) ? (openBlock(), createElementBlock("div", _hoisted_7$e, [
            createBaseVNode("div", _hoisted_8$9, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(unref(businessData), (business, index) => {
                return openBlock(), createElementBlock("div", { key: index }, [
                  createVNode(Card, {
                    cardItem: business,
                    canSelect: "",
                    needPublish: "",
                    onItemClick: ($event) => handleBusinessClick(business),
                    "onUpdate:isSelected": ($event) => updateBusinessIsSelected(index, $event)
                  }, null, 8, ["cardItem", "onItemClick", "onUpdate:isSelected"])
                ]);
              }), 128))
            ])
          ])) : createCommentVNode("", true)
        ])
      ], 64);
    };
  }
});/* unplugin-vue-components disabled */const MyComponent = /* @__PURE__ */ _export_sfc(_sfc_main$B, [["__scopeId", "data-v-f4e2f79e"]]);const _hoisted_1$z = { class: "collapse-block" };
const _hoisted_2$t = { class: "collapse-title" };
const _hoisted_3$q = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _sfc_main$A = /* @__PURE__ */ defineComponent({
  __name: "MyScenario",
  setup(__props) {
    const router = useRouter();
    onActivated(() => {
      scenarioList.value = [
        {
          domainCode: "SmartBuilding",
          domainName: "智慧楼宇",
          collapse: false,
          children: [
            {
              code: "BuildingA",
              name: "交叉二号楼",
              imageUrl: getAssetsFile("logo.png"),
              isSelected: false
            },
            {
              code: "BuildingB",
              name: "物理楼",
              imageUrl: getAssetsFile("logo.png"),
              isSelected: false
            }
          ]
        },
        {
          domainCode: "SmartMine",
          domainName: "智慧矿山",
          collapse: false,
          children: []
        }
      ];
    });
    const state = reactive({
      scenarioList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        }
      ]
    });
    const { scenarioList, dropDownItems } = toRefs(state);
    const updateIsSelected = (index, value) => {
      scenarioList.value[index].isSelected = value;
    };
    const handleCommand = (scenario, command) => {
      console.log("Clicked item:", scenario, command);
    };
    const handleClick = (scenario) => {
      console.log(scenario);
      router.push({ path: "/developer/scenario/detail", query: { scenarioId: scenario.code, scenarioName: scenario.name } });
    };
    return (_ctx, _cache) => {
      return openBlock(true), createElementBlock(Fragment, null, renderList(unref(scenarioList), (domain, index) => {
        return openBlock(), createElementBlock("div", { key: index }, [
          createBaseVNode("div", _hoisted_1$z, [
            createBaseVNode("div", _hoisted_2$t, toDisplayString(domain.domainName + "(" + domain.children.length + ")"), 1)
          ]),
          createBaseVNode("div", _hoisted_3$q, [
            (openBlock(true), createElementBlock(Fragment, null, renderList(domain.children, (scenario, index2) => {
              return openBlock(), createBlock(Card, {
                key: index2,
                cardItem: scenario,
                canSelect: "",
                dropDownItems: unref(dropDownItems),
                onCommandClick: ($event) => handleCommand(scenario, $event),
                onItemClick: ($event) => handleClick(scenario),
                "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
              }, null, 8, ["cardItem", "dropDownItems", "onCommandClick", "onItemClick", "onUpdate:isSelected"]);
            }), 128))
          ])
        ]);
      }), 128);
    };
  }
});/* unplugin-vue-components disabled */const MyScenario = /* @__PURE__ */ _export_sfc(_sfc_main$A, [["__scopeId", "data-v-0bd3944b"]]);const useApplicationStore = defineStore("application", {
  state: () => ({
    applicationId: "",
    applicationName: ""
  }),
  getters: {
    getApplicationId: (state) => state.applicationId,
    getApplicationName: (state) => state.applicationName
  },
  actions: {
    loadApplication(application) {
      this.applicationId = application.applicationId;
      this.applicationName = application.applicationName;
    }
  },
  persist: true
});const _hoisted_1$y = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _sfc_main$z = /* @__PURE__ */ defineComponent({
  __name: "MyApplication",
  setup(__props) {
    const router = useRouter();
    onActivated(() => {
      applicationList.value = [
        {
          code: "CoffeeReception",
          name: "自助咖啡",
          imageUrl: getAssetsFile("logo.png"),
          isSelected: false
        }
      ];
    });
    const state = reactive({
      applicationList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        },
        {
          code: "edit",
          name: "编辑"
        },
        {
          code: "scan",
          name: "扫描"
        }
      ]
    });
    const { applicationList, dropDownItems } = toRefs(state);
    const updateIsSelected = (index, value) => {
      applicationList.value[index].isSelected = value;
    };
    const handleCommand = (application, command) => {
      console.log("Clicked item:", application, command);
    };
    const applicationStore = useApplicationStore();
    const handleClick = (application) => {
      console.log(application);
      applicationStore.loadApplication({
        applicationId: application.code,
        applicationName: application.name
      });
      const fullPath = router.resolve(`/design/index?applicationId=${application.code}&applicationName=${application.name}`).href;
      window.open(fullPath, "_blank");
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", _hoisted_1$y, [
        (openBlock(true), createElementBlock(Fragment, null, renderList(unref(applicationList), (application, index) => {
          return openBlock(), createElementBlock("div", { key: index }, [
            createVNode(Card, {
              cardItem: application,
              canSelect: "",
              dropDownItems: unref(dropDownItems),
              onCommandClick: ($event) => handleCommand(application, $event),
              onItemClick: ($event) => handleClick(application),
              "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
            }, null, 8, ["cardItem", "dropDownItems", "onCommandClick", "onItemClick", "onUpdate:isSelected"])
          ]);
        }), 128))
      ]);
    };
  }
});const _hoisted_1$x = { style: { "position": "relative" } };
const _hoisted_2$s = {
  key: 0,
  class: "add-button"
};
const _sfc_main$y = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const userStore = useUserStore();
    const { roles } = storeToRefs(userStore);
    const activeName = ref("component");
    const router = useRouter();
    const myComponent = ref(null);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.active === "string") {
        activeName.value = router.currentRoute.value.query.active || "component";
      }
      if (activeName.value === "component") {
        console.log(myComponent);
        if (myComponent.value) {
          myComponent.value.getComponentData();
        }
      }
    });
    const handleClick = (tab, event) => {
      router.push({ query: { ...router.currentRoute.value.query, active: tab.props.name } });
    };
    return (_ctx, _cache) => {
      const _component_el_tab_pane = ElTabPane;
      const _component_el_tabs = ElTabs;
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      const _component_el_dropdown_item = ElDropdownItem;
      const _component_el_dropdown_menu = ElDropdownMenu;
      const _component_el_dropdown = ElDropdown;
      return openBlock(), createElementBlock("div", _hoisted_1$x, [
        createVNode(_component_el_tabs, {
          modelValue: activeName.value,
          "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => activeName.value = $event),
          class: "workspace-tabs",
          onTabClick: handleClick
        }, {
          default: withCtx(() => [
            unref(roles).includes("admin") ? (openBlock(), createBlock(_component_el_tab_pane, {
              key: 0,
              label: "我的控件",
              name: "component",
              class: "tab-pane"
            }, {
              default: withCtx(() => [
                createVNode(MyComponent, {
                  ref_key: "myComponent",
                  ref: myComponent
                }, null, 512)
              ]),
              _: 1
            })) : createCommentVNode("", true),
            unref(roles).includes("developer") || unref(roles).includes("admin") ? (openBlock(), createBlock(_component_el_tab_pane, {
              key: 1,
              label: "我的领域",
              name: "domain",
              class: "tab-pane"
            }, {
              default: withCtx(() => [
                createVNode(_sfc_main$C)
              ]),
              _: 1
            })) : createCommentVNode("", true),
            unref(roles).includes("developer") || unref(roles).includes("admin") ? (openBlock(), createBlock(_component_el_tab_pane, {
              key: 2,
              label: "我的场景",
              name: "scenario",
              class: "tab-pane"
            }, {
              default: withCtx(() => [
                createVNode(MyScenario)
              ]),
              _: 1
            })) : createCommentVNode("", true),
            unref(roles).includes("business") ? (openBlock(), createBlock(_component_el_tab_pane, {
              key: 3,
              label: "我的应用",
              name: "application",
              class: "tab-pane"
            }, {
              default: withCtx(() => [
                createVNode(_sfc_main$z)
              ]),
              _: 1
            })) : createCommentVNode("", true)
          ]),
          _: 1
        }, 8, ["modelValue"]),
        activeName.value !== "component" ? (openBlock(), createElementBlock("div", _hoisted_2$s, [
          createVNode(_component_el_dropdown, null, {
            dropdown: withCtx(() => [
              createVNode(_component_el_dropdown_menu, null, {
                default: withCtx(() => [
                  createVNode(_component_el_dropdown_item, null, {
                    default: withCtx(() => [
                      createTextVNode("新增")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_dropdown_item, null, {
                    default: withCtx(() => [
                      createTextVNode("从模板库导入")
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            default: withCtx(() => [
              createVNode(_component_el_button, { type: "primary" }, {
                default: withCtx(() => [
                  createVNode(_component_el_icon, {
                    size: "18",
                    color: "white"
                  }, {
                    default: withCtx(() => [
                      createVNode(unref(plus_default))
                    ]),
                    _: 1
                  }),
                  createTextVNode("  添加")
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ])) : createCommentVNode("", true)
      ]);
    };
  }
});/* unplugin-vue-components disabled *//* unplugin-vue-components disabled */const WorkspaceView = /* @__PURE__ */ _export_sfc(_sfc_main$y, [["__scopeId", "data-v-8d942fa0"]]);const _hoisted_1$w = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _sfc_main$x = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const router = useRouter();
    onActivated(() => {
      applicationList.value = [
        {
          code: "GuestReception",
          name: "来访接待",
          imageUrl: new URL("/index/img/logo.png", import.meta.url).href,
          isSelected: false,
          route: "/demo/application/detail?applicationId=GuestReception&applicationName=来访接待&scenarioName=交叉二号楼"
        }
      ];
    });
    const state = reactive({
      applicationList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        }
      ]
    });
    const { applicationList } = toRefs(state);
    const handleClick = (application) => {
      console.log(application);
      const fullPath = router.resolve(application.route).href;
      window.open(fullPath, "_blank");
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", _hoisted_1$w, [
        (openBlock(true), createElementBlock(Fragment, null, renderList(unref(applicationList), (application, index) => {
          return openBlock(), createElementBlock("div", { key: index }, [
            createVNode(Card, {
              cardItem: application,
              onItemClick: ($event) => handleClick(application)
            }, null, 8, ["cardItem", "onItemClick"])
          ]);
        }), 128))
      ]);
    };
  }
});const _hoisted_1$v = { style: { "padding": "20px" } };
const _hoisted_2$r = /* @__PURE__ */ createBaseVNode("div", { class: "el-upload__text" }, [
  /* @__PURE__ */ createTextVNode(" 将文件拖入框中或者 "),
  /* @__PURE__ */ createBaseVNode("em", null, "点击上传")
], -1);
const _hoisted_3$p = /* @__PURE__ */ createBaseVNode("div", { class: "el-upload__tip" }, " jpg/png 文件大小应小于2MB ", -1);
const _hoisted_4$n = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _sfc_main$w = /* @__PURE__ */ defineComponent({
  __name: "Device_Information",
  props: {
    info: Object
  },
  emits: ["update-info"],
  setup(__props, { emit: __emit }) {
    const uploadRef = ref();
    const uploadUrl = "http://139.196.147.52:8080/file/upload";
    const emit = __emit;
    const props = __props;
    const deviceFormRef = ref();
    const rules = reactive({
      deviceCode: [
        { required: true, message: "请输入设备码", trigger: "blur" }
      ],
      deviceName: [
        { required: true, message: "请输入设备名称", trigger: "blur" }
      ]
      // imageUrl:[
      //   {required: true, message:'请输入上传图标', trigger:'blur'},
      // ]
    });
    const deviceForm = reactive({
      deviceCode: "",
      deviceName: "",
      imageUrl: ""
    });
    watchEffect(() => {
      if (props.info) {
        deviceForm.deviceCode = props.info.deviceCode;
        deviceForm.deviceName = props.info.deviceName;
        deviceForm.imageUrl = props.info.imageUrl;
      }
    });
    const resetForm = () => {
      if (deviceFormRef) {
        deviceFormRef.value.resetFields();
      }
      return;
    };
    const submitForm = async (formEl) => {
      if (!formEl)
        return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          uploadRef.value.submit();
          emit("update-info", deviceForm);
          console.log("submit!", fields, deviceForm);
        }
      });
    };
    const beforeAvatarUpload = (rawFile) => {
      console.log(rawFile.type);
      if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/jpg" && rawFile.type !== "image/png") {
        ElMessage.error("Picture must be JPG format!");
        return false;
      } else if (rawFile.size / 1024 / 1024 > 2) {
        ElMessage.error("Picture size can not exceed 2MB!");
        return false;
      }
      return true;
    };
    const handleAvatarUpload = (res) => {
      deviceForm.imageUrl = res;
      console.log(deviceForm.imageUrl);
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_image = ElImage;
      const _component_el_icon = ElIcon;
      const _component_el_upload = ElUpload;
      const _component_el_button = ElButton;
      const _component_el_form = ElForm;
      return openBlock(), createElementBlock("div", null, [
        createBaseVNode("div", _hoisted_1$v, [
          createVNode(_component_el_form, {
            rules: unref(rules),
            model: unref(deviceForm),
            ref_key: "deviceFormRef",
            ref: deviceFormRef,
            "label-width": "auto",
            "label-position": "left",
            style: { "max-width": "800px", "margin": "auto" }
          }, {
            default: withCtx(() => [
              createVNode(_component_el_form_item, {
                label: "设备码",
                prop: "deviceCode"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    modelValue: unref(deviceForm).deviceCode,
                    "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => unref(deviceForm).deviceCode = $event),
                    placeholder: "请输入"
                  }, null, 8, ["modelValue"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "设备名称",
                prop: "deviceName"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    modelValue: unref(deviceForm).deviceName,
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(deviceForm).deviceName = $event),
                    placeholder: "请输入"
                  }, null, 8, ["modelValue"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "图标上传",
                prop: "imageUrl"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_upload, {
                    drag: "",
                    multiple: "",
                    ref_key: "uploadRef",
                    ref: uploadRef,
                    class: "avatar-uploader",
                    "show-file-list": true,
                    "before-upload": beforeAvatarUpload,
                    "on-success": handleAvatarUpload,
                    action: `${uploadUrl}?deviceCode=${unref(deviceForm).deviceCode}`,
                    "auto-upload": false
                  }, {
                    default: withCtx(() => [
                      unref(deviceForm).imageUrl ? (openBlock(), createBlock(_component_el_image, {
                        key: 0,
                        src: unref(deviceForm).imageUrl,
                        class: "avatar"
                      }, null, 8, ["src"])) : (openBlock(), createBlock(_component_el_icon, {
                        key: 1,
                        class: "avatar-uploader-icon"
                      }, {
                        default: withCtx(() => [
                          createVNode(unref(upload_filled_default))
                        ]),
                        _: 1
                      })),
                      _hoisted_2$r,
                      _hoisted_3$p
                    ]),
                    _: 1
                  }, 8, ["action"])
                ]),
                _: 1
              }),
              createBaseVNode("div", _hoisted_4$n, [
                createVNode(_component_el_button, {
                  type: "primary",
                  onClick: _cache[2] || (_cache[2] = ($event) => submitForm(unref(deviceFormRef))),
                  style: { "margin-left": "auto" }
                }, {
                  default: withCtx(() => [
                    createTextVNode(" 下一步 ")
                  ]),
                  _: 1
                }),
                createVNode(_component_el_button, {
                  onClick: resetForm,
                  style: { "margin-right": "auto" }
                }, {
                  default: withCtx(() => [
                    createTextVNode("重置")
                  ]),
                  _: 1
                })
              ])
            ]),
            _: 1
          }, 8, ["rules", "model"])
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled *//* unplugin-vue-components disabled */const _hoisted_1$u = { key: 2 };
const _hoisted_2$q = { style: { "display": "flex", "gap": "8px", "align-items": "center" } };
const _hoisted_3$o = {
  key: 0,
  style: { "margin-top": "20px" }
};
const _hoisted_4$m = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _sfc_main$v = /* @__PURE__ */ defineComponent({
  __name: "Table",
  props: {
    data: Array,
    header: Array,
    canChoose: {
      type: Boolean,
      default: false
    },
    canEdit: {
      type: Boolean,
      default: true
    },
    canDelete: {
      type: Boolean,
      default: true
    },
    buttonGroup: Array
  },
  emits: ["handleEdit", "handleDelete", "handleLinkClick", "button-click"],
  setup(__props, { emit: __emit }) {
    const tableRef = ref(null);
    const state = reactive({
      imgVisible: false,
      selectedImage: ""
    });
    const { imgVisible, selectedImage } = toRefs(state);
    const clearSelection = () => {
      tableRef.value.clearSelection();
    };
    const emit = __emit;
    const handleEdit = (index, row) => {
      emit("handleEdit", row);
    };
    const handleDelete = (index, row) => {
      emit("handleDelete", row);
    };
    const handleImageClick = (imageUrl) => {
      selectedImage.value = imageUrl;
      imgVisible.value = true;
    };
    const handleLinkClick = (link) => {
      emit("handleLinkClick", link);
    };
    const handleDialogClose = () => {
      selectedImage.value = "";
    };
    const handleClick = (code) => {
      emit("button-click", code);
    };
    return (_ctx, _cache) => {
      const _component_el_table_column = ElTableColumn;
      const _component_el_image = ElImage;
      const _component_el_button = ElButton;
      const _component_ElButton = ElButton;
      const _component_el_table = ElTable;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(_component_el_table, {
          ref_key: "tableRef",
          ref: tableRef,
          data: __props.data,
          height: "auto",
          border: "",
          stripe: "",
          style: { "width": "100%" },
          "header-cell-style": { background: "#eef1f6", color: "#606266" }
        }, {
          default: withCtx(() => [
            __props.canChoose ? (openBlock(), createBlock(_component_el_table_column, {
              key: 0,
              type: "selection",
              width: "55"
            })) : createCommentVNode("", true),
            (openBlock(true), createElementBlock(Fragment, null, renderList(__props.header, (col) => {
              return openBlock(), createBlock(_component_el_table_column, {
                key: col.code,
                prop: col.code,
                label: col.name
              }, {
                default: withCtx((scope) => [
                  col.type === "Image" ? (openBlock(), createBlock(_component_el_image, {
                    key: 0,
                    src: scope.row[col.code],
                    alt: "Image",
                    style: { "width": "50px", "height": "50px", "cursor": "pointer" },
                    onClick: ($event) => handleImageClick(scope.row[col.code])
                  }, null, 8, ["src", "onClick"])) : createCommentVNode("", true),
                  col.type === "Link" ? (openBlock(), createBlock(_component_el_button, {
                    key: 1,
                    link: "",
                    onClick: ($event) => handleLinkClick(scope.row[col.code]),
                    style: { "color": "#50a5fb" }
                  }, {
                    default: withCtx(() => [
                      createTextVNode(toDisplayString(scope.row[col.code]), 1)
                    ]),
                    _: 2
                  }, 1032, ["onClick"])) : createCommentVNode("", true),
                  col.type !== "Image" && col.type !== "Link" ? (openBlock(), createElementBlock("span", _hoisted_1$u, toDisplayString(scope.row[col.code]), 1)) : createCommentVNode("", true)
                ]),
                _: 2
              }, 1032, ["prop", "label"]);
            }), 128)),
            __props.canDelete || __props.canEdit ? (openBlock(), createBlock(_component_el_table_column, {
              key: 1,
              label: "操作"
            }, {
              default: withCtx((scope) => [
                createBaseVNode("div", _hoisted_2$q, [
                  (openBlock(true), createElementBlock(Fragment, null, renderList(__props.buttonGroup, (item) => {
                    return openBlock(), createElementBlock("div", {
                      key: item.code,
                      style: { "margin-right": "10px" }
                    }, [
                      createVNode(_component_ElButton, {
                        type: item.type,
                        onClick: ($event) => handleClick(item.code),
                        size: item.size
                      }, {
                        default: withCtx(() => [
                          createTextVNode(toDisplayString(item.name), 1)
                        ]),
                        _: 2
                      }, 1032, ["type", "onClick", "size"])
                    ]);
                  }), 128)),
                  __props.canEdit ? (openBlock(), createBlock(_component_el_button, {
                    key: 0,
                    size: "small",
                    onClick: ($event) => handleEdit(scope.$index, scope.row)
                  }, {
                    default: withCtx(() => [
                      createTextVNode(" 编辑 ")
                    ]),
                    _: 2
                  }, 1032, ["onClick"])) : createCommentVNode("", true),
                  __props.canDelete ? (openBlock(), createBlock(_component_el_button, {
                    key: 1,
                    size: "small",
                    type: "danger",
                    onClick: ($event) => handleDelete(scope.$index, scope.row)
                  }, {
                    default: withCtx(() => [
                      createTextVNode(" 删除 ")
                    ]),
                    _: 2
                  }, 1032, ["onClick"])) : createCommentVNode("", true)
                ])
              ]),
              _: 1
            })) : createCommentVNode("", true)
          ]),
          _: 1
        }, 8, ["data"]),
        __props.canChoose ? (openBlock(), createElementBlock("div", _hoisted_3$o, [
          createBaseVNode("div", _hoisted_4$m, [
            createVNode(_component_el_button, {
              type: "primary",
              style: { "margin-left": "auto" },
              plain: ""
            }, {
              default: withCtx(() => [
                createTextVNode("保存选择")
              ]),
              _: 1
            }),
            createVNode(_component_el_button, {
              type: "primary",
              style: { "margin-right": "auto" },
              onClick: _cache[0] || (_cache[0] = ($event) => clearSelection()),
              plain: ""
            }, {
              default: withCtx(() => [
                createTextVNode("清除选择")
              ]),
              _: 1
            })
          ])
        ])) : createCommentVNode("", true),
        createVNode(_component_el_dialog, {
          modelValue: unref(imgVisible),
          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(imgVisible) ? imgVisible.value = $event : null),
          width: "50%",
          onClose: handleDialogClose
        }, {
          default: withCtx(() => [
            createVNode(_component_el_image, {
              src: unref(selectedImage),
              alt: "Expanded Image",
              style: { "width": "100%" }
            }, null, 8, ["src"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const initDeviceType = (deviceCode, deviceName, imageUrl) => service({
  url: `/add-device-type/init`,
  method: "post",
  data: {
    deviceCode,
    deviceName,
    imageUrl
  }
});
const addDeviceTypeOperation = (deviceType, command) => service({
  url: `/add-device-type/operation/add`,
  method: "post",
  data: {
    deviceType,
    command
  }
});
const addDeviceTypeEvent = (deviceType, commandCode, deviceEvent) => service({
  url: `/add-device-type/event/add`,
  method: "post",
  data: {
    deviceType,
    commandCode,
    deviceEvent
  }
});
const addDeviceTypeService = (deviceType, brandService) => service({
  url: `/add-device-type/service/init`,
  method: "post",
  data: {
    deviceType,
    brandService
  }
});
const uploadDeviceType = (data) => service({
  url: `/device-type/upload`,
  method: "post",
  data
});
const getOperationParam = (deviceName, commandCode) => service({
  url: `/load-operation-param?deviceName=${deviceName}&commandCode=${commandCode}`,
  method: "get"
});
const getOperationEvent = (deviceName, operationCode) => service({
  url: `/load-operation-event?deviceName=${deviceName}&operationCode=${operationCode}`,
  method: "get"
});
const getOperationCommand = (deviceType) => service({
  url: `/load-operation-command?deviceType=${deviceType}`,
  method: "get"
});
const getService = (deviceName, serviceName) => service({
  url: `/load-service?deviceName=${deviceName}&serviceName=${serviceName}`,
  method: "get"
});
const loadDeviceInfo = (deviceTypeCode) => service({
  url: `/device-type/info?deviceTypeCode=${deviceTypeCode}`,
  method: "get"
});
const updateDevicePublish = (deviceTypeCode) => service({
  url: `/device-type/publish?deviceTypeCode=${deviceTypeCode}`,
  method: `put`
});var define_import_meta_env_default$8 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$t = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_2$p = /* @__PURE__ */ createBaseVNode("div", {
  id: "功能列表",
  style: { "margin-bottom": "20px" }
}, "操作列表", -1);
const _hoisted_3$n = {
  slot: "title",
  style: { "font-size": "large" }
};
const _hoisted_4$l = { style: { "padding": "20px" } };
const _hoisted_5$i = /* @__PURE__ */ createBaseVNode("div", {
  id: "操作信息编辑",
  style: { "margin-top": "10px", "margin-bottom": "20px" }
}, "操作信息编辑", -1);
const _hoisted_6$f = { style: { "display": "flex", "justify-content": "space-between", "width": "600px" } };
const _hoisted_7$d = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_8$8 = /* @__PURE__ */ createBaseVNode("div", {
  id: "公共事件",
  style: { "margin-top": "10px" }
}, "公共事件", -1);
const _hoisted_9$7 = /* @__PURE__ */ createBaseVNode("div", { style: { "display": "flex", "justify-content": "space-between" } }, [
  /* @__PURE__ */ createBaseVNode("div", {
    id: "已绑定事件",
    style: { "margin-bottom": "20px", "font-size": "large" }
  }, "绑定事件")
], -1);
const _hoisted_10$4 = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_11$2 = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_12$1 = /* @__PURE__ */ createBaseVNode("div", {
  id: "新增操作",
  style: { "margin-top": "10px", "margin-bottom": "20px", "font-size": "large" }
}, "新增操作", -1);
const _hoisted_13 = { style: { "display": "flex", "justify-content": "space-between", "width": "700px" } };
const _hoisted_14 = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _sfc_main$u = /* @__PURE__ */ defineComponent({
  __name: "Function_Set",
  props: {
    name: String
  },
  emits: ["operation-info"],
  setup(__props, { emit: __emit }) {
    const eventFormRef = ref();
    const operationFormRef = ref();
    const event_rules = reactive({
      event_Name: [
        { required: true, message: "请输入事件名称", trigger: "blur" }
      ],
      event_Description: [
        { required: true, message: "请输入事件描述", trigger: "blur" }
      ],
      event_Type: [
        { required: true, message: "请选择事件类型", trigger: "blur" }
      ],
      signature: [
        { required: true, message: "请输入函数名", trigger: "blur" }
      ],
      event_Args: [
        { required: true, message: "请选择所需参数声明", trigger: "blur" }
      ]
    });
    const operation_add_rules = reactive({
      operation_Code: [
        { required: true, message: "请输入操作代码", trigger: "blur" }
      ],
      operation_Name: [
        { required: true, message: "请输入操作名称", trigger: "blur" }
      ]
    });
    const operation_change_rules = reactive({
      operation_Code: [
        { required: false, message: "请输入操作代码", trigger: "blur" }
      ],
      operation_Name: [
        { required: false, message: "请输入操作名称", trigger: "blur" }
      ]
    });
    const showEventForm = () => {
      if (event_data.value.length === 3) {
        ElMessage.warning("当前已无可绑定事件");
      } else {
        eventsVisible.value = true;
      }
    };
    const EventForm = reactive({
      eventType_Is_Chosen: {
        onStart: true,
        onComplete: true,
        onError: true
      },
      event_Name: "",
      event_Description: "",
      event_Type: "",
      signature: "",
      event_Args: ""
    });
    const state = reactive({
      event_header: [
        {
          code: "name",
          name: "事件名称",
          type: "String"
        },
        {
          code: "description",
          name: "事件描述",
          type: "String"
        },
        {
          code: "type",
          name: "事件类型",
          type: "String"
        }
      ],
      event_data: [],
      header: [
        {
          code: "code",
          name: "操作码",
          type: "String"
        },
        {
          code: "name",
          name: "操作名称",
          type: "String"
        },
        {
          code: "events_count",
          name: "已绑定事件",
          type: "Int"
        }
      ],
      data: [],
      dialogVisible: false,
      eventsVisible: false,
      operationVisible: false,
      selectedService: null,
      dia_title: "编辑操作",
      service_header: [],
      service_data: []
    });
    const outputParams = [
      {
        value: "void",
        label: "void"
      },
      {
        value: "Object",
        label: "Object"
      }
    ];
    const OperationForm = reactive({
      operation_Code: "",
      operation_Name: "",
      Param_header: [
        {
          code: "code",
          name: "变量码",
          type: "String"
        },
        {
          code: "name",
          name: "变量名",
          type: "String"
        },
        {
          code: "type",
          name: "变量数据类型",
          type: "any"
        }
      ],
      operation_InputParam: [],
      operation_OutputParam: ""
    });
    const { event_header, event_data, header, data, dialogVisible, eventsVisible, operationVisible, dia_title, selectedService } = toRefs(state);
    const { operation_InputParam, operation_OutputParam } = toRefs(OperationForm);
    onMounted(() => {
      if (define_import_meta_env_default$8.VITE_MODE === "mock") {
        operation_InputParam.value = [{
          code: "coffeeType",
          name: "咖啡类型",
          type: "String"
        }];
        event_data.value = [{
          event_name: "onMakeCoffeeStart",
          event_description: "prepare the coffee"
        }];
      }
      getCommandData();
    });
    const emit = __emit;
    const props = __props;
    watchEffect(() => {
      if (props.name) {
        console.log("Device_name:", props.name);
      } else {
        console.log("Can't receive device_name", props);
      }
    });
    const onEdit = (row) => {
      dialogVisible.value = true;
      dia_title.value = row.name;
      selectedService.value = row;
      console.log(row);
      OperationForm.operation_Code = row.code;
      OperationForm.operation_Name = row.name;
      getEventData();
      getParams();
    };
    const submitForm = async (formEl) => {
      if (!formEl)
        return;
      await formEl.validate((_valid, fields) => {
        if (eventFormRef !== void 0) {
          const event = {
            name: EventForm.event_Name,
            type: EventForm.event_Type,
            description: EventForm.event_Description
          };
          console.log(event);
          addDeviceTypeEvent(props.name, OperationForm.operation_Code, event).then((res) => {
            if (res.status === 200) {
              ElMessage.success("新增事件成功");
              state.eventsVisible = false;
              eventFormRef.value?.resetFields();
              getEventData();
              getCommandData();
            }
          });
        } else {
          console.log("error submit!", fields);
        }
      });
    };
    const resetForm = () => {
      if (eventFormRef) {
        eventFormRef.value?.resetFields();
      }
    };
    const beforeOperationEditClose = () => {
      OperationForm.operation_Code = "";
      OperationForm.operation_Name = "";
      OperationForm.operation_InputParam = [];
      OperationForm.operation_OutputParam = "";
      event_data.value = [];
    };
    const editEventFile = (fileName) => {
      console.log(fileName);
    };
    const getCommandData = () => {
      getOperationCommand(props.name).then((res) => {
        if (res.status === 200) {
          state.data = res.data.map((v) => {
            return {
              code: v.commandCode,
              name: v.commandName,
              events_count: v.events.length + "/3"
            };
          });
        }
      });
    };
    const getParams = () => {
      getOperationParam(props.name, selectedService.value.code).then((res) => {
        if (res.status === 200) {
          operation_InputParam.value = res.data.inputParams;
          operation_OutputParam.value = res.data.outputParam;
        }
      });
    };
    const getEventData = () => {
      EventForm.eventType_Is_Chosen.onStart = true;
      EventForm.eventType_Is_Chosen.onComplete = true;
      EventForm.eventType_Is_Chosen.onError = true;
      getOperationEvent(props.name, selectedService.value.code).then((res) => {
        if (res.status === 200) {
          event_data.value = res.data;
          res.data.forEach((v) => {
            const type = v.type;
            if (type === "onStart") {
              EventForm.eventType_Is_Chosen.onStart = false;
            } else if (type === "onComplete") {
              EventForm.eventType_Is_Chosen.onComplete = false;
            } else if (type === "onError") {
              EventForm.eventType_Is_Chosen.onError = false;
            }
          });
        }
      });
    };
    const CommitOperation = async (formEl) => {
      operationVisible.value = false;
      if (!formEl)
        return;
      await formEl.validate((_valid, fields) => {
        if (operationFormRef) {
          console.log("submit! operation", OperationForm);
          emit("operation-info", OperationForm);
          const operation = {
            commandCode: OperationForm.operation_Code,
            commandName: OperationForm.operation_Name,
            inputParam: OperationForm.operation_InputParam,
            outputParam: OperationForm.operation_OutputParam
          };
          addDeviceTypeOperation(props.name, operation).then((res) => {
            if (res.status === 200) {
              getCommandData();
            }
          });
        } else {
          console.log("error submit!", fields);
        }
      });
    };
    const Commit = () => {
      console.log("commit");
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_form = ElForm;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", null, [
          createBaseVNode("div", _hoisted_1$t, [
            _hoisted_2$p,
            createBaseVNode("div", null, "——" + toDisplayString(props.name), 1),
            createVNode(_component_el_button, {
              onClick: _cache[0] || (_cache[0] = ($event) => operationVisible.value = true),
              type: "primary",
              style: { "margin-left": "auto" }
            }, {
              default: withCtx(() => [
                createTextVNode("新增操作")
              ]),
              _: 1
            })
          ]),
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data),
            canChoose: false,
            onHandleEdit: onEdit
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[12] || (_cache[12] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          width: "50%",
          onClose: beforeOperationEditClose
        }, {
          default: withCtx(() => [
            createBaseVNode("span", _hoisted_3$n, "操作名称：" + toDisplayString(unref(dia_title)), 1),
            createBaseVNode("div", _hoisted_4$l, [
              createBaseVNode("div", null, [
                _hoisted_5$i,
                createVNode(_component_el_form, {
                  rules: unref(operation_change_rules),
                  model: unref(OperationForm),
                  ref_key: "operationFormRef",
                  ref: operationFormRef,
                  "label-width": "auto",
                  "label-position": "left",
                  style: { "max-width": "800px", "margin": "auto" }
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, {
                      label: "操作码",
                      prop: "operation_Code"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(OperationForm).operation_Code,
                          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(OperationForm).operation_Code = $event),
                          placeholder: unref(selectedService)?.code || "请输入操作码"
                        }, null, 8, ["modelValue", "placeholder"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "操作名称",
                      prop: "operation_Name"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(OperationForm).operation_Name,
                          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(OperationForm).operation_Name = $event),
                          placeholder: unref(selectedService)?.name || "请输入操作名称"
                        }, null, 8, ["modelValue", "placeholder"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "输入参数设定",
                      prop: "operation_InputParam"
                    }, {
                      default: withCtx(() => [
                        createBaseVNode("div", _hoisted_6$f, [
                          createVNode(_component_el_button, {
                            onClick: _cache[3] || (_cache[3] = () => {
                            }),
                            type: "primary",
                            style: { "margin-left": "auto", "margin-bottom": "20px" },
                            plain: ""
                          }, {
                            default: withCtx(() => [
                              createTextVNode("新增输入参数")
                            ]),
                            _: 1
                          })
                        ]),
                        createVNode(_sfc_main$v, {
                          header: unref(OperationForm).Param_header,
                          data: unref(OperationForm).operation_InputParam
                        }, null, 8, ["header", "data"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "输出参数设定",
                      prop: "operation_OutputParam"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: unref(operation_OutputParam),
                          "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => isRef(operation_OutputParam) ? operation_OutputParam.value = $event : null),
                          placeholder: "请选择输出参数",
                          style: { "width": "350px" }
                        }, {
                          default: withCtx(() => [
                            (openBlock(), createElementBlock(Fragment, null, renderList(outputParams, (item) => {
                              return createVNode(_component_el_option, {
                                key: item.value,
                                label: item.label,
                                value: item.value
                              }, null, 8, ["label", "value"]);
                            }), 64))
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["rules", "model"])
              ]),
              createBaseVNode("div", _hoisted_7$d, [
                _hoisted_8$8,
                createVNode(_component_el_button, {
                  onClick: showEventForm,
                  type: "primary",
                  style: { "margin-left": "auto", "margin-bottom": "20px" },
                  plain: ""
                }, {
                  default: withCtx(() => [
                    createTextVNode("绑定事件")
                  ]),
                  _: 1
                })
              ]),
              createVNode(_sfc_main$v, {
                header: unref(event_header),
                data: unref(event_data),
                onHandleLinkClick: _cache[5] || (_cache[5] = ($event) => editEventFile($event))
              }, null, 8, ["header", "data"]),
              createVNode(_component_el_dialog, {
                modelValue: unref(eventsVisible),
                "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => isRef(eventsVisible) ? eventsVisible.value = $event : null),
                width: "40%",
                "append-to-body": ""
              }, {
                default: withCtx(() => [
                  _hoisted_9$7,
                  createVNode(_component_el_form, {
                    rules: unref(event_rules),
                    model: unref(EventForm),
                    ref_key: "eventFormRef",
                    ref: eventFormRef,
                    "label-width": "auto",
                    "label-position": "left",
                    style: { "max-width": "800px", "margin": "auto" }
                  }, {
                    default: withCtx(() => [
                      createVNode(_component_el_form_item, {
                        label: "事件名称",
                        prop: "event_Name"
                      }, {
                        default: withCtx(() => [
                          createVNode(_component_el_input, {
                            modelValue: unref(EventForm).event_Name,
                            "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => unref(EventForm).event_Name = $event),
                            placeholder: "请输入"
                          }, null, 8, ["modelValue"])
                        ]),
                        _: 1
                      }),
                      createVNode(_component_el_form_item, {
                        label: "事件描述",
                        prop: "event_Description"
                      }, {
                        default: withCtx(() => [
                          createVNode(_component_el_input, {
                            modelValue: unref(EventForm).event_Description,
                            "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => unref(EventForm).event_Description = $event),
                            placeholder: "请输入"
                          }, null, 8, ["modelValue"])
                        ]),
                        _: 1
                      }),
                      createVNode(_component_el_form_item, {
                        label: "事件类型",
                        prop: "event_Type"
                      }, {
                        default: withCtx(() => [
                          createVNode(_component_el_select, {
                            modelValue: unref(EventForm).event_Type,
                            "onUpdate:modelValue": _cache[8] || (_cache[8] = ($event) => unref(EventForm).event_Type = $event),
                            placeholder: "请选择你的事件类型"
                          }, {
                            default: withCtx(() => [
                              createVNode(_component_el_option, {
                                label: "操作开始事件",
                                value: "onStart",
                                disabled: !unref(EventForm).eventType_Is_Chosen.onStart
                              }, null, 8, ["disabled"]),
                              createVNode(_component_el_option, {
                                label: "操作完成事件",
                                value: "onComplete",
                                disabled: !unref(EventForm).eventType_Is_Chosen.onComplete
                              }, null, 8, ["disabled"]),
                              createVNode(_component_el_option, {
                                label: "操作错误事件",
                                value: "onError",
                                disabled: !unref(EventForm).eventType_Is_Chosen.onError
                              }, null, 8, ["disabled"])
                            ]),
                            _: 1
                          }, 8, ["modelValue"])
                        ]),
                        _: 1
                      }),
                      createBaseVNode("div", _hoisted_10$4, [
                        createVNode(_component_el_button, {
                          type: "primary",
                          onClick: _cache[9] || (_cache[9] = ($event) => submitForm(unref(eventFormRef))),
                          style: { "margin-left": "auto" }
                        }, {
                          default: withCtx(() => [
                            createTextVNode(" 确认 ")
                          ]),
                          _: 1
                        }),
                        createVNode(_component_el_button, {
                          onClick: resetForm,
                          style: { "margin-right": "auto" }
                        }, {
                          default: withCtx(() => [
                            createTextVNode("重置")
                          ]),
                          _: 1
                        })
                      ])
                    ]),
                    _: 1
                  }, 8, ["rules", "model"])
                ]),
                _: 1
              }, 8, ["modelValue"])
            ]),
            createBaseVNode("div", _hoisted_11$2, [
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-left": "auto" },
                onClick: Commit
              }, {
                default: withCtx(() => [
                  createTextVNode("确定")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-right": "auto" },
                onClick: _cache[11] || (_cache[11] = ($event) => dialogVisible.value = false),
                plain: ""
              }, {
                default: withCtx(() => [
                  createTextVNode("返回")
                ]),
                _: 1
              })
            ])
          ]),
          _: 1
        }, 8, ["modelValue"]),
        createVNode(_component_el_dialog, {
          modelValue: unref(operationVisible),
          "onUpdate:modelValue": _cache[19] || (_cache[19] = ($event) => isRef(operationVisible) ? operationVisible.value = $event : null),
          width: " 50%"
        }, {
          default: withCtx(() => [
            _hoisted_12$1,
            createVNode(_component_el_form, {
              rules: unref(operation_add_rules),
              model: unref(OperationForm),
              ref_key: "operationFormRef",
              ref: operationFormRef,
              "label-width": "auto",
              "label-position": "left",
              style: { "max-width": "800px", "margin": "auto" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, {
                  label: "操作代码",
                  prop: "operation_Code"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(OperationForm).operation_Code,
                      "onUpdate:modelValue": _cache[13] || (_cache[13] = ($event) => unref(OperationForm).operation_Code = $event),
                      placeholder: "请输入操作代码"
                    }, null, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "操作名称",
                  prop: "operation_Name"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(OperationForm).operation_Name,
                      "onUpdate:modelValue": _cache[14] || (_cache[14] = ($event) => unref(OperationForm).operation_Name = $event),
                      placeholder: "请输入操作名称"
                    }, null, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "输入参数设定",
                  prop: "operation_InputParam"
                }, {
                  default: withCtx(() => [
                    createBaseVNode("div", _hoisted_13, [
                      createVNode(_component_el_button, {
                        onClick: _cache[15] || (_cache[15] = () => {
                        }),
                        type: "primary",
                        style: { "margin-left": "auto", "margin-bottom": "20px" },
                        plain: ""
                      }, {
                        default: withCtx(() => [
                          createTextVNode("新增输入参数")
                        ]),
                        _: 1
                      })
                    ]),
                    createVNode(_sfc_main$v, {
                      header: unref(OperationForm).Param_header,
                      data: unref(OperationForm).operation_InputParam
                    }, null, 8, ["header", "data"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "输出参数设定",
                  prop: "operation_OutputParam"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_select, {
                      modelValue: unref(operation_OutputParam),
                      "onUpdate:modelValue": _cache[16] || (_cache[16] = ($event) => isRef(operation_OutputParam) ? operation_OutputParam.value = $event : null),
                      placeholder: "请选择输出参数",
                      style: { "width": "350px" }
                    }, {
                      default: withCtx(() => [
                        (openBlock(), createElementBlock(Fragment, null, renderList(outputParams, (item) => {
                          return createVNode(_component_el_option, {
                            key: item.value,
                            label: item.label,
                            value: item.value
                          }, null, 8, ["label", "value"]);
                        }), 64))
                      ]),
                      _: 1
                    }, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                createBaseVNode("div", _hoisted_14, [
                  createVNode(_component_el_button, {
                    type: "primary",
                    style: { "margin-left": "auto" },
                    onClick: _cache[17] || (_cache[17] = ($event) => CommitOperation(unref(operationFormRef)))
                  }, {
                    default: withCtx(() => [
                      createTextVNode("确定")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    style: { "margin-right": "auto" },
                    onClick: _cache[18] || (_cache[18] = ($event) => operationVisible.value = false)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("返回")
                    ]),
                    _: 1
                  })
                ])
              ]),
              _: 1
            }, 8, ["rules", "model"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});var define_import_meta_env_default$7 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$s = { style: { "display": "flex", "justify-content": "space-between", "margin-top": "20px" } };
const _hoisted_2$o = /* @__PURE__ */ createBaseVNode("div", {
  id: "支持服务",
  style: { "margin-top": "10px" }
}, "设备支持", -1);
const _hoisted_3$m = /* @__PURE__ */ createBaseVNode("div", { style: { "display": "flex", "justify-content": "space-between" } }, [
  /* @__PURE__ */ createBaseVNode("div", {
    id: "新增设备支持",
    style: { "margin-bottom": "20px", "font-size": "large" }
  }, "新增设备支持")
], -1);
const _hoisted_4$k = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_5$h = /* @__PURE__ */ createBaseVNode("div", { style: { "display": "flex", "justify-content": "space-between" } }, [
  /* @__PURE__ */ createBaseVNode("div", {
    id: "编辑设备支持",
    style: { "margin-bottom": "20px", "font-size": "large" }
  }, "设备支持编辑")
], -1);
const _hoisted_6$e = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _sfc_main$t = /* @__PURE__ */ defineComponent({
  __name: "Service_Set",
  props: {
    name: String,
    info: Object
  },
  emits: ["service-info"],
  setup(__props, { emit: __emit }) {
    const emit = __emit;
    const serviceFormRef = ref();
    const ServiceForm = reactive({
      factory_Code: "",
      factory_Name: "",
      factory_Description: "",
      fileName: ""
    });
    const edit_rules = reactive({
      factory_Name: [
        { required: false, message: "请输入厂家名称", trigger: "blur" }
      ],
      factory_Description: [
        { required: false, message: "请输入服务描述", trigger: "blur" }
      ]
    });
    const service_rules = reactive({
      factory_Code: [
        { required: true, message: "请输入厂家代码", trigger: "blur" }
      ],
      factory_Name: [
        { required: true, message: "请输入厂家名称", trigger: "blur" }
      ],
      factory_Description: [
        { required: true, message: "请输入服务描述", trigger: "blur" }
      ]
    });
    const state = reactive({
      service_header: [
        {
          code: "name",
          name: "厂商名称",
          type: "String"
        },
        {
          code: "description",
          name: "支持服务描述",
          type: "String"
        },
        {
          code: "filename",
          name: "服务定义文件",
          type: "Link"
        }
      ],
      service_data: [],
      serviceVisible: false,
      dialogVisible: false,
      dia_title: "",
      selectedService: null
      // 初始化为空
    });
    const onEdit = (row) => {
      dialogVisible.value = true;
      state.selectedService = row;
      dia_title.value = row.name;
      ServiceForm.factory_Code = row.code;
      ServiceForm.factory_Name = row.name;
      ServiceForm.factory_Description = row.description;
      ServiceForm.fileName = row.filename;
    };
    const { service_header, service_data, serviceVisible, dialogVisible, dia_title, selectedService } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default$7.VITE_MODE === "mock") {
        service_data.value = [{
          name: "A公司",
          description: "A公司咖啡机可以做（摩卡，美式）咖啡，支持（加糖，加奶）操作。",
          filename: "AService.json"
        }];
      } else {
        getServiceData();
      }
    });
    const props = __props;
    const submitForm = async (formEl) => {
      if (!formEl)
        return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          emit("service-info", ServiceForm);
          const service = {
            code: ServiceForm.factory_Code,
            name: ServiceForm.factory_Name,
            description: ServiceForm.factory_Description,
            filename: ServiceForm.factory_Code + ".json"
          };
          console.log(service);
          addDeviceTypeService(props.name, service).then((res) => {
            if (res.status === 200) {
              ElMessage.success("新增品牌成功");
              state.serviceVisible = false;
              serviceFormRef.value.resetFields();
              getServiceData();
            }
          });
        } else {
          console.log("error submit!", fields);
        }
      });
    };
    const resetForm = () => {
      if (serviceFormRef) {
        serviceFormRef.value.resetFields();
      }
      return;
    };
    const getServiceData = () => {
      console.log("Try to get service data!", props.info);
      getService(props.name, "AService").then((res) => {
        if (res.status === 200) {
          service_data.value = res.data;
        }
      });
    };
    const changeForm = () => {
      console.log("change the form");
    };
    const goToFile = (filename) => {
      const routeUrl = router.resolve({ path: "developer/workspace" });
      window.open(routeUrl.href, "_blank");
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$s, [
          _hoisted_2$o,
          createVNode(_component_el_button, {
            onClick: _cache[0] || (_cache[0] = ($event) => serviceVisible.value = true),
            type: "primary",
            style: { "margin-left": "auto", "margin-bottom": "20px" }
          }, {
            default: withCtx(() => [
              createTextVNode("新增设备支持")
            ]),
            _: 1
          })
        ]),
        createVNode(_sfc_main$v, {
          header: unref(service_header),
          data: unref(service_data),
          onHandleEdit: onEdit
        }, null, 8, ["header", "data"]),
        createVNode(_component_el_dialog, {
          modelValue: unref(serviceVisible),
          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => isRef(serviceVisible) ? serviceVisible.value = $event : null),
          width: "40%",
          "append-to-body": ""
        }, {
          default: withCtx(() => [
            _hoisted_3$m,
            createVNode(_component_el_form, {
              rules: unref(service_rules),
              model: unref(ServiceForm),
              ref_key: "serviceFormRef",
              ref: serviceFormRef,
              "label-width": "auto",
              "label-position": "left",
              style: { "max-width": "800px", "margin": "auto" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, {
                  label: "厂家代码",
                  prop: "factory_Code"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(ServiceForm).factory_Code,
                      "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(ServiceForm).factory_Code = $event),
                      placeholder: unref(selectedService)?.factory_Code || "请输入厂家代码"
                    }, null, 8, ["modelValue", "placeholder"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "厂家名称",
                  prop: "factory_Name"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(ServiceForm).factory_Name,
                      "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(ServiceForm).factory_Name = $event),
                      placeholder: "请输入"
                    }, null, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "服务描述",
                  prop: "factory_Description"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(ServiceForm).factory_Description,
                      "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => unref(ServiceForm).factory_Description = $event),
                      placeholder: "请输入"
                    }, null, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                createBaseVNode("div", _hoisted_4$k, [
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: _cache[4] || (_cache[4] = ($event) => submitForm(unref(serviceFormRef))),
                    style: { "margin-left": "auto" }
                  }, {
                    default: withCtx(() => [
                      createTextVNode(" 确认 ")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    onClick: resetForm,
                    style: { "margin-right": "auto" }
                  }, {
                    default: withCtx(() => [
                      createTextVNode("重置")
                    ]),
                    _: 1
                  })
                ])
              ]),
              _: 1
            }, 8, ["rules", "model"])
          ]),
          _: 1
        }, 8, ["modelValue"]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          width: "40%",
          "append-to-body": ""
        }, {
          default: withCtx(() => [
            _hoisted_5$h,
            createVNode(_component_el_form, {
              rules: unref(edit_rules),
              model: unref(ServiceForm),
              ref_key: "serviceFormRef",
              ref: serviceFormRef,
              "label-width": "auto",
              "label-position": "left",
              style: { "max-width": "800px", "margin": "auto" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, {
                  label: "厂家名称",
                  prop: "factory_Name"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(ServiceForm).factory_Name,
                      "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => unref(ServiceForm).factory_Name = $event),
                      placeholder: unref(selectedService)?.factory_Name || "请输入厂家名称"
                    }, null, 8, ["modelValue", "placeholder"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, {
                  label: "服务描述",
                  prop: "factory_Description"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(ServiceForm).factory_Description,
                      "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => unref(ServiceForm).factory_Description = $event),
                      placeholder: unref(selectedService)?.factory_Description || "请输入服务描述"
                    }, null, 8, ["modelValue", "placeholder"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_form_item, null, {
                  default: withCtx(() => [
                    createBaseVNode("div", null, [
                      createTextVNode(" 点击"),
                      createVNode(_component_el_button, {
                        link: "",
                        style: { "color": "#50a5fb" },
                        onClick: _cache[8] || (_cache[8] = ($event) => goToFile(unref(ServiceForm).fileName))
                      }, {
                        default: withCtx(() => [
                          createTextVNode("此处")
                        ]),
                        _: 1
                      }),
                      createTextVNode("编辑文件 ")
                    ])
                  ]),
                  _: 1
                }),
                createBaseVNode("div", _hoisted_6$e, [
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: _cache[9] || (_cache[9] = ($event) => changeForm()),
                    style: { "margin-left": "auto" }
                  }, {
                    default: withCtx(() => [
                      createTextVNode(" 确认 ")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    onClick: resetForm,
                    style: { "margin-right": "auto" }
                  }, {
                    default: withCtx(() => [
                      createTextVNode("重置")
                    ]),
                    _: 1
                  })
                ])
              ]),
              _: 1
            }, 8, ["rules", "model"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const _hoisted_1$r = { style: { "font-size": "16px" } };
const _hoisted_2$n = /* @__PURE__ */ createBaseVNode("span", null, "新增设备类型", -1);
const _hoisted_3$l = { style: { "margin-top": "30px" } };
const _hoisted_4$j = { style: { "margin-right": "40px", "display": "flex", "justify-content": "end" } };
const _sfc_main$s = /* @__PURE__ */ defineComponent({
  __name: "AddDeviceType",
  setup(__props) {
    const router = useRouter();
    const state = reactive({
      active: 0,
      deviceName: "",
      deviceInfo: {},
      operationCode: [],
      operationInfo: [],
      factoryCode: [],
      factoryInfo: []
    });
    const { active, deviceName, deviceInfo, operationCode, operationInfo, factoryCode, factoryInfo } = toRefs(state);
    const prev = () => {
      active.value--;
    };
    const next = () => {
      if (active.value++ > 2)
        active.value = 0;
    };
    const updateDeviceInfo = (new_device) => {
      deviceName.value = new_device.deviceCode;
      deviceInfo.value = new_device;
      initDeviceType(new_device.deviceCode, new_device.deviceName, new_device.imageUrl ?? "").then((res) => {
        if (res.status === 200) {
          next();
        }
      });
    };
    const updateOperation = (new_operation) => {
      operationInfo.value.push(new_operation);
      operationCode.value.push(new_operation.operation_Code);
      console.log(new_operation.operation_Code);
    };
    const updateService = (new_service) => {
      factoryInfo.value.push(new_service);
      factoryCode.value.push(new_service.factory_Code);
      console.log(new_service.factory_Code);
    };
    const UploadDeviceInfo = () => {
      uploadDeviceType({
        deviceTypeCode: deviceInfo.value.deviceCode,
        deviceTypeName: deviceInfo.value.deviceName,
        imgPath: deviceInfo.value.imageUrl
      }).then(async (res) => {
        if (res.status === 200) {
          console.log("DeviceInfo posted successfully", res.data);
          ElMessage.success("上传设备类型成功");
          await router.push("/developer/workspace");
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      const _component_el_step = ElStep;
      const _component_el_steps = ElSteps;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$r, [
          createVNode(_component_el_button, {
            link: "",
            onClick: _cache[0] || (_cache[0] = ($event) => unref(router).go(-1)),
            style: { "margin-right": "10px" }
          }, {
            default: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(arrow_left_default))
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          _hoisted_2$n
        ]),
        createVNode(_component_el_steps, {
          style: { "margin": "30px" },
          active: unref(active)
        }, {
          default: withCtx(() => [
            createVNode(_component_el_step, {
              title: "Step 1",
              description: "信息录入",
              icon: unref(picture_default)
            }, null, 8, ["icon"]),
            createVNode(_component_el_step, {
              title: "Step 2",
              description: "功能设置",
              icon: unref(setting_default)
            }, null, 8, ["icon"]),
            createVNode(_component_el_step, {
              title: "Step 3",
              description: "品牌设置",
              icon: unref(edit_default)
            }, null, 8, ["icon"])
          ]),
          _: 1
        }, 8, ["active"]),
        createBaseVNode("div", _hoisted_3$l, [
          unref(active) === 0 ? (openBlock(), createBlock(_sfc_main$w, {
            key: 0,
            onUpdateInfo: updateDeviceInfo,
            info: unref(deviceInfo)
          }, null, 8, ["info"])) : createCommentVNode("", true),
          unref(active) === 1 ? (openBlock(), createBlock(_sfc_main$u, {
            key: 1,
            onOperationInfo: updateOperation,
            name: unref(deviceName)
          }, null, 8, ["name"])) : createCommentVNode("", true),
          unref(active) === 2 ? (openBlock(), createBlock(_sfc_main$t, {
            key: 2,
            onServiceInfo: updateService,
            name: unref(deviceName),
            info: unref(deviceInfo)
          }, null, 8, ["name", "info"])) : createCommentVNode("", true)
        ]),
        createBaseVNode("div", _hoisted_4$j, [
          unref(active) !== 0 ? (openBlock(), createBlock(_component_el_button, {
            key: 0,
            style: { "margin-left": "12px", "margin-top": "20px" },
            onClick: prev
          }, {
            default: withCtx(() => [
              createTextVNode("上一步")
            ]),
            _: 1
          })) : createCommentVNode("", true),
          unref(active) !== 2 && unref(active) !== 0 ? (openBlock(), createBlock(_component_el_button, {
            key: 1,
            type: "primary",
            style: { "margin-left": "12px", "margin-top": "20px" },
            onClick: next
          }, {
            default: withCtx(() => [
              createTextVNode("下一步")
            ]),
            _: 1
          })) : createCommentVNode("", true),
          unref(active) === 2 ? (openBlock(), createBlock(_component_el_button, {
            key: 2,
            type: "primary",
            style: { "margin-left": "12px", "margin-top": "20px" },
            onClick: UploadDeviceInfo
          }, {
            default: withCtx(() => [
              createTextVNode("完成")
            ]),
            _: 1
          })) : createCommentVNode("", true)
        ])
      ], 64);
    };
  }
});var define_import_meta_env_default$6 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$q = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_2$m = { style: { "font-size": "18px", "margin-bottom": "30px" } };
const _hoisted_3$k = { style: { "font-size": "16px", "margin-bottom": "15px" } };
const _hoisted_4$i = { style: { "margin": "20px" } };
const _hoisted_5$g = /* @__PURE__ */ createBaseVNode("div", { class: "cell-item" }, " 设备类型代码 ", -1);
const _hoisted_6$d = /* @__PURE__ */ createBaseVNode("div", { class: "cell-item" }, " 设备类型名 ", -1);
const _hoisted_7$c = /* @__PURE__ */ createBaseVNode("div", { class: "cell-item" }, " 图标 ", -1);
const _hoisted_8$7 = { style: { "margin": "50px 20px 20px" } };
const _hoisted_9$6 = /* @__PURE__ */ createBaseVNode("div", { style: { "font-weight": "bold", "margin-bottom": "20px" } }, "功能服务", -1);
const _hoisted_10$3 = { style: { "margin-bottom": "10px", "display": "flex", "justify-content": "end" } };
const _hoisted_11$1 = { style: { "margin-bottom": "10px", "display": "flex", "justify-content": "end" } };
const _hoisted_12 = { style: { "margin-bottom": "10px", "display": "flex", "justify-content": "end" } };
const _sfc_main$r = /* @__PURE__ */ defineComponent({
  __name: "DeviceComponentDetail",
  setup(__props) {
    const state = reactive({
      deviceCode: "",
      deviceName: "",
      deviceData: {},
      commandData: [],
      eventData: [],
      serviceData: [],
      dialogVisible: false,
      progress: 0,
      progressMessage: "开始打包发布",
      progressImage: ""
    });
    const { deviceCode, deviceName, deviceData, commandData, eventData, serviceData, dialogVisible, progress, progressMessage, progressImage } = toRefs(state);
    const router = useRouter();
    const route = useRoute();
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.deviceCode === "string") {
        deviceCode.value = router.currentRoute.value.query.deviceCode || "";
      }
      if (typeof router.currentRoute.value.query.deviceName === "string") {
        deviceName.value = router.currentRoute.value.query.deviceName || "";
      }
    });
    onMounted(() => {
      if (define_import_meta_env_default$6.VITE_MODE === "mock") {
        deviceData.value = {
          code: "CoffeeMaker",
          name: "咖啡机器人",
          imageUrl: new URL("/index/img/CoffeeMaker.png", import.meta.url).href
        };
        commandData.value = [
          {
            commandCode: "MakeCoffee",
            commandName: "做咖啡",
            eventNum: 3,
            serviceNum: 2
          }
        ];
        eventData.value = [
          {
            name: "onMakeCoffeeStart",
            description: "准备咖啡",
            commandId: "MakeCoffee",
            type: "onStart"
          },
          {
            name: "onMakeCoffeeComplete",
            description: "咖啡完成提醒",
            commandId: "MakeCoffee",
            type: "onComplete"
          },
          {
            name: "onMakeCoffeeError",
            description: "错误警告",
            commandId: "MakeCoffee",
            type: "onError"
          }
        ];
        serviceData.value = [
          {
            code: "AService",
            name: "A品牌",
            description: "A公司咖啡机可以做（摩卡，美式）咖啡，支持（加糖，加奶）操作。",
            filename: "AService.json"
          }
        ];
      } else {
        loadDeviceData();
      }
    });
    watch(route, (newRoute, oldRoute) => {
      loadDeviceData();
    });
    const goToCodeServer = () => {
      window.open(`http://139.196.147.52:5200/?folder=/home/coder/project/workplace/deviceType/${deviceCode.value}/definitions/`, "_blank");
    };
    const loadDeviceData = () => {
      commandData.value = [];
      serviceData.value = [];
      eventData.value = [];
      getDeviceData();
      getCommandData();
      getEventData();
      getServiceData();
    };
    const commandHeader = [
      {
        code: "commandCode",
        name: "操作码",
        type: "String"
      },
      {
        code: "commandName",
        name: "操作名称",
        type: "String"
      },
      {
        code: "eventNum",
        name: "已绑定事件",
        type: "String"
      },
      {
        code: "serviceNum",
        name: "支持品牌",
        type: "Number"
      }
    ];
    const eventHeader = [
      {
        code: "name",
        name: "事件名称",
        type: "String"
      },
      {
        code: "description",
        name: "事件描述",
        type: "String"
      },
      {
        code: "commandId",
        name: "绑定操作",
        type: "String"
      },
      {
        code: "type",
        name: "事件类型",
        type: "String"
      }
    ];
    const serviceHeader = [
      {
        code: "name",
        name: "厂商名称",
        type: "String"
      },
      {
        code: "description",
        name: "厂商描述",
        type: "String"
      },
      {
        code: "filename",
        name: "厂商定义文件",
        type: "Link"
      }
    ];
    const getDeviceData = () => {
      loadDeviceInfo(deviceCode.value).then(async (res) => {
        if (res.status === 200) {
          deviceData.value = {
            code: res.data.deviceTypeCode,
            name: res.data.deviceTypeName,
            imageUrl: getAssetsFile("device/" + res.data.deviceTypeCode + ".png")
          };
        }
      });
    };
    const getCommandData = () => {
      getOperationCommand(deviceCode.value).then((res) => {
        if (res.status === 200) {
          commandData.value = res.data.map((v) => {
            return {
              commandCode: v.commandCode,
              commandName: v.commandName,
              eventNum: (v.events?.length ?? 0) + "/3",
              serviceNum: v.services?.length ?? 0
            };
          });
        }
      });
    };
    const getServiceData = () => {
      getService(deviceCode.value, "AService").then((res) => {
        if (res.status === 200) {
          serviceData.value = res.data;
        }
      });
    };
    const getEventData = () => {
      getOperationEvent(deviceCode.value, "").then((res) => {
        if (res.status === 200) {
          eventData.value = res.data;
        }
      });
    };
    const startPublish = () => {
      progress.value = 0;
      progressImage.value = getAssetsFile("progress/generate.png");
      const eventSource = new EventSource(`http://139.196.147.52:8080/device/publish?deviceType=${deviceData.value.code}`);
      eventSource.onmessage = (event) => {
        const progressData = JSON.parse(event.data);
        console.log(progressData);
        progressMessage.value = progressData.message;
        progress.value = progressData.progress;
        if (progress.value < 50) {
          progressImage.value = getAssetsFile("progress/generate.png");
        } else if (progress.value < 75) {
          progressImage.value = getAssetsFile("progress/package.png");
        } else {
          progressImage.value = getAssetsFile("progress/copy.png");
        }
        if (progress.value === 100) {
          updateDevicePublish(deviceData.value.code).then((res) => {
            if (res.status === 200) {
              ElMessage.success("设备发布成功");
            }
          });
        }
      };
      eventSource.onerror = () => {
        eventSource.close();
      };
    };
    const customColorMethod = (percentage) => {
      if (percentage < 30) {
        return "#909399";
      }
      if (percentage < 70) {
        return "#e6a23c";
      }
      return "#67c23a";
    };
    const editServiceFile = (fileName) => {
      console.log(fileName);
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_button = ElButton;
      const _component_el_image = ElImage;
      const _component_el_progress = ElProgress;
      const _component_el_dialog = ElDialog;
      const _component_el_descriptions_item = ElDescriptionsItem;
      const _component_el_descriptions = ElDescriptions;
      const _component_el_tab_pane = ElTabPane;
      const _component_el_tabs = ElTabs;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$q, [
          createBaseVNode("div", _hoisted_2$m, [
            createVNode(_component_el_button, {
              link: "",
              onClick: _cache[0] || (_cache[0] = ($event) => unref(router).go(-1)),
              style: { "margin-right": "10px" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_icon, null, {
                  default: withCtx(() => [
                    createVNode(unref(arrow_left_default))
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }),
            createTextVNode(" 设备类型详情-" + toDisplayString(unref(deviceName)), 1)
          ]),
          createVNode(_component_el_button, {
            type: "primary",
            onClick: _cache[1] || (_cache[1] = ($event) => {
              dialogVisible.value = true;
              startPublish();
            })
          }, {
            default: withCtx(() => [
              createTextVNode("发布")
            ]),
            _: 1
          })
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "打包发布",
          width: "500"
        }, {
          default: withCtx(() => [
            createVNode(_component_el_image, {
              src: unref(progressImage),
              style: { "width": "90%", "margin-left": "5%" }
            }, null, 8, ["src"]),
            createBaseVNode("div", _hoisted_3$k, toDisplayString(unref(progressMessage)), 1),
            createVNode(_component_el_progress, {
              percentage: unref(progress),
              color: customColorMethod
            }, null, 8, ["percentage"])
          ]),
          _: 1
        }, 8, ["modelValue"]),
        createBaseVNode("div", _hoisted_4$i, [
          createVNode(_component_el_descriptions, {
            title: "基本信息",
            column: 2,
            border: ""
          }, {
            extra: withCtx(() => [
              createVNode(_component_el_button, {
                type: "primary",
                plain: "",
                onClick: _cache[3] || (_cache[3] = ($event) => goToCodeServer())
              }, {
                default: withCtx(() => [
                  createTextVNode("编辑")
                ]),
                _: 1
              })
            ]),
            default: withCtx(() => [
              createVNode(_component_el_descriptions_item, null, {
                label: withCtx(() => [
                  _hoisted_5$g
                ]),
                default: withCtx(() => [
                  createTextVNode(" " + toDisplayString(unref(deviceData).code), 1)
                ]),
                _: 1
              }),
              createVNode(_component_el_descriptions_item, null, {
                label: withCtx(() => [
                  _hoisted_6$d
                ]),
                default: withCtx(() => [
                  createTextVNode(" " + toDisplayString(unref(deviceData).name), 1)
                ]),
                _: 1
              }),
              createVNode(_component_el_descriptions_item, null, {
                label: withCtx(() => [
                  _hoisted_7$c
                ]),
                default: withCtx(() => [
                  createVNode(_component_el_image, {
                    src: unref(deviceData).imageUrl,
                    style: { "width": "10%" }
                  }, null, 8, ["src"])
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_8$7, [
          _hoisted_9$6,
          createVNode(_component_el_tabs, { type: "card" }, {
            default: withCtx(() => [
              createVNode(_component_el_tab_pane, { label: "操作" }, {
                default: withCtx(() => [
                  createBaseVNode("div", _hoisted_10$3, [
                    createVNode(_component_el_button, {
                      type: "primary",
                      plain: ""
                    }, {
                      default: withCtx(() => [
                        createTextVNode("新增")
                      ]),
                      _: 1
                    })
                  ]),
                  createVNode(_sfc_main$v, {
                    header: commandHeader,
                    data: unref(commandData)
                  }, null, 8, ["data"])
                ]),
                _: 1
              }),
              createVNode(_component_el_tab_pane, { label: "事件" }, {
                default: withCtx(() => [
                  createBaseVNode("div", _hoisted_11$1, [
                    createVNode(_component_el_button, {
                      type: "primary",
                      plain: ""
                    }, {
                      default: withCtx(() => [
                        createTextVNode("新增")
                      ]),
                      _: 1
                    })
                  ]),
                  createVNode(_sfc_main$v, {
                    header: eventHeader,
                    data: unref(eventData)
                  }, null, 8, ["data"])
                ]),
                _: 1
              }),
              createVNode(_component_el_tab_pane, { label: "品牌" }, {
                default: withCtx(() => [
                  createBaseVNode("div", _hoisted_12, [
                    createVNode(_component_el_button, {
                      type: "primary",
                      plain: ""
                    }, {
                      default: withCtx(() => [
                        createTextVNode("新增")
                      ]),
                      _: 1
                    })
                  ]),
                  createVNode(_sfc_main$v, {
                    header: serviceHeader,
                    data: unref(serviceData),
                    onHandleLinkClick: _cache[4] || (_cache[4] = ($event) => editServiceFile($event))
                  }, null, 8, ["data"])
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ])
      ], 64);
    };
  }
});const getScenarioJson = () => service({
  url: `/load-scenario-json`,
  method: "get"
});
const loadScenarioBindingData = (componentType, domainCode) => service({
  url: `/load-domain-component-binding?componentType=${componentType}&domainCode=${domainCode}`,
  method: "get"
});
const uploadDeviceData = (data) => service({
  url: `/device/data`,
  method: "post",
  data
});
const uploadDeviceRegisterData = (data, scenarioCode, deviceName) => service({
  url: `/register/device?scenarioCode=${scenarioCode}&deviceName=${deviceName}`,
  method: "post",
  data
});
const loadScenarioData = (scenarioCode) => service({
  url: `/register/device?scenarioCode=${scenarioCode}`,
  method: "get"
});var define_import_meta_env_default$5 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$p = {
  class: "scenario-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$l = /* @__PURE__ */ createBaseVNode("div", { id: "场景地图" }, "场景地图", -1);
const _hoisted_3$j = { class: "scenario-content" };
const _hoisted_4$h = ["src"];
const _hoisted_5$f = {
  class: "scenario-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_6$c = /* @__PURE__ */ createBaseVNode("div", { id: "区域列表" }, "区域列表", -1);
const _hoisted_7$b = { class: "scenario-content" };
const _sfc_main$q = /* @__PURE__ */ defineComponent({
  __name: "DetailArea",
  props: {
    scenarioId: String,
    scenarioName: String
  },
  setup(__props) {
    const state = reactive({
      header: [],
      data: [],
      mapPath: "map.png"
    });
    const { header, data, mapPath } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default$5.VITE_MODE === "mock") {
        header.value = [{
          code: "floor",
          name: "楼层",
          type: "String"
        }, {
          code: "description",
          name: "描述",
          type: "String"
        }, {
          code: "planPath",
          name: "平面图",
          type: "Image"
        }];
        data.value = [{
          floor: "二层",
          description: "交叉二号楼二楼",
          planPath: getAssetsFile("images/floor.jpeg")
        }];
      } else {
        getDomainField();
      }
    });
    const getScenarioData = () => {
      getScenarioJson().then((res) => {
        if (res.status === 200) {
          mapPath.value = res.data.mapPath;
          data.value = res.data.maplist.map((v) => {
            header.value.forEach((item) => {
              if (item.type === "Image" && v[item.code]) {
                const imgPath = "images/" + v[item.code];
                v[item.code] = getAssetsFile(imgPath);
              }
            });
            return v;
          });
        }
      });
    };
    const getDomainField = () => {
      getDomainJson().then((res) => {
        if (res.status === 200) {
          header.value = res.data.domainField;
          getScenarioData();
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", null, [
          createBaseVNode("div", _hoisted_1$p, [
            _hoisted_2$l,
            createVNode(_component_el_button, { type: "primary" }, {
              default: withCtx(() => [
                createTextVNode("导入地图")
              ]),
              _: 1
            })
          ]),
          createBaseVNode("div", _hoisted_3$j, [
            createBaseVNode("img", {
              src: unref(getAssetsFile)("images/" + unref(mapPath)),
              alt: "场景地图",
              style: { "width": "80%", "margin-left": "10%" }
            }, null, 8, _hoisted_4$h)
          ])
        ]),
        createBaseVNode("div", null, [
          createBaseVNode("div", _hoisted_5$f, [
            _hoisted_6$c,
            createVNode(_component_el_button, { type: "primary" }, {
              default: withCtx(() => [
                createTextVNode("新增区域")
              ]),
              _: 1
            })
          ]),
          createBaseVNode("div", _hoisted_7$b, [
            createVNode(_sfc_main$v, {
              header: unref(header),
              data: unref(data)
            }, null, 8, ["header", "data"])
          ])
        ])
      ], 64);
    };
  }
});var define_import_meta_env_default$4 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$o = {
  class: "scenario-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$k = /* @__PURE__ */ createBaseVNode("div", { id: "设备" }, "设备", -1);
const _hoisted_3$i = { class: "scenario-content" };
const _hoisted_4$g = { key: 0 };
const _hoisted_5$e = { style: { "display": "flex", "justify-content": "space-between", "font-size": "16px", "margin-bottom": "20px", "margin-top": "10px" } };
const _hoisted_6$b = /* @__PURE__ */ createBaseVNode("div", null, "领域设备类型", -1);
const _hoisted_7$a = { style: { "color": "#50a5fb" } };
const _hoisted_8$6 = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _hoisted_9$5 = { key: 1 };
const _hoisted_10$2 = { style: { "padding": "20px" } };
const _sfc_main$p = /* @__PURE__ */ defineComponent({
  __name: "DetailDevice",
  props: {
    scenarioId: String,
    scenarioName: String
  },
  setup(__props) {
    const state = reactive({
      header: [
        {
          code: "deviceCode",
          name: "设备码",
          type: "String"
        },
        {
          code: "deviceName",
          name: "设备名",
          type: "String"
        },
        {
          code: "deviceType",
          name: "设备类型",
          type: "String"
        },
        {
          code: "deviceService",
          name: "品牌/厂商",
          type: "String"
        },
        {
          code: "protocol",
          name: "协议",
          //TCP/IP,HTTP
          type: "String"
        },
        {
          code: "host",
          name: "主机名/ip地址",
          type: "String"
        },
        {
          code: "port",
          name: "端口号",
          type: "Number"
        }
      ],
      data: [],
      domainDevice: [],
      dialogVisible: false,
      selectedDevice: "",
      searchInput: ""
    });
    const { header, data, domainDevice, dialogVisible, selectedDevice, searchInput } = toRefs(state);
    const deviceFormRef = ref();
    const deviceForm = reactive({
      deviceCode: "",
      deviceName: "",
      deviceType: "",
      deviceService: {
        code: "",
        name: ""
      },
      protocol: "",
      host: "",
      port: ""
    });
    const rules = reactive({
      deviceCode: [
        { required: true, message: "请填写设备码", trigger: "blur" }
      ],
      deviceName: [
        { required: true, message: "请填写设备名", trigger: "blur" }
      ],
      "deviceType.code": [
        { required: true, message: "请选择设备类型", trigger: "blur" }
      ],
      "deviceService.code": [
        { required: false, message: "请选择设备品牌/厂商", trigger: "blur" }
      ],
      protocol: [
        { required: true, message: "请选择协议", trigger: "blur" }
      ],
      host: [
        { required: true, message: "请填写主机名或ip地址", trigger: "blur" }
      ],
      port: [
        { required: true, message: "请填写端口号", trigger: "blur" }
      ]
    });
    onMounted(() => {
      if (define_import_meta_env_default$4.VITE_MODE === "mock") {
        data.value = [
          {
            deviceCode: "deviceId",
            deviceName: "咖啡机器人A",
            deviceType: "咖啡机器人",
            deviceService: "A品牌",
            protocol: "HTTP",
            host: "http://aservice.coffee",
            port: 8080
          },
          {
            deviceCode: "deviceId2",
            deviceName: "咖啡机器人B",
            deviceType: "咖啡机器人",
            deviceService: "B品牌",
            protocol: "HTTP",
            host: "http://bservice.coffee",
            port: 8080
          }
        ];
        domainDevice.value = [
          {
            code: "CoffeeMaker",
            name: "咖啡机器人",
            services: [
              {
                code: "AService",
                name: "A品牌"
              },
              {
                code: "BService",
                name: "B品牌"
              }
            ],
            imageUrl: getAssetsFile("device/CoffeeMaker.png")
          },
          {
            code: "AirConditioner",
            name: "空调",
            services: [],
            imageUrl: getAssetsFile("device/AirConditioner.png")
          }
        ];
      } else {
        getScenarioDevice();
      }
    });
    const getScenarioDevice = () => {
      loadScenarioBindingData("Device", "SmartBuilding").then((res) => {
        if (res.status === 200) {
          domainDevice.value = res.data.map((v) => {
            return {
              code: v.deviceTypeCode,
              name: v.deviceTypeName,
              //todo:此处应该读取json文件，暂时写死
              services: [
                {
                  code: "AService",
                  name: "A品牌"
                },
                {
                  code: "BService",
                  name: "B品牌"
                }
              ],
              imageUrl: getAssetsFile("device/" + v.deviceTypeCode + ".png")
            };
          });
          data.value = [];
          loadScenarioData("The second interdisciplinary building").then((res2) => {
            if (res2.status === 200) {
              const devices = res2.data;
              devices.forEach((device) => {
                const matchedDevice = domainDevice.value.find((d) => d.code === device.deviceType);
                const serviceName = matchedDevice?.services.find((service) => service.code === device.deviceService)?.name || device.deviceService;
                const newDevice = {
                  deviceCode: device.deviceCode,
                  deviceName: device.deviceName,
                  deviceType: device.deviceType,
                  deviceService: serviceName,
                  protocol: device.protocol,
                  host: device.host,
                  port: device.port
                };
                data.value.push(newDevice);
              });
            }
          });
        }
      });
    };
    const handleDomainDeviceClick = (device) => {
      selectedDevice.value = device;
      deviceForm.deviceType = device;
    };
    const handleReturnDomainDevice = () => {
      if (deviceFormRef) {
        deviceFormRef.value.resetFields();
      }
      selectedDevice.value = "";
    };
    const submitForm = async (formEl) => {
      if (!formEl)
        return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          uploadDeviceData({ deviceForm }).then((res) => {
            if (res.status === 200) {
              ElMessage.success("设备实例上传成功");
              uploadDeviceRegisterData({ deviceForm }, "The second interdisciplinary building", deviceForm.deviceName).then(async (res2) => {
                if (res2.status === 200) {
                  ElMessage.success("设备实例注册成功");
                  await getScenarioDevice();
                  console.log("submit!", deviceForm);
                  selectedDevice.value = "";
                  deviceForm.deviceCode = "";
                  deviceForm.deviceName = "";
                  deviceForm.deviceType = "";
                  deviceForm.protocol = "";
                  deviceForm.host = "";
                  deviceForm.port = "";
                }
              });
            }
          });
        } else {
          console.log("error submit!", fields);
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_form = ElForm;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$o, [
          _hoisted_2$k,
          createVNode(_component_el_button, {
            type: "primary",
            onClick: _cache[0] || (_cache[0] = ($event) => dialogVisible.value = true)
          }, {
            default: withCtx(() => [
              createTextVNode("新增设备")
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_3$i, [
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data)
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "新增设备",
          width: "50%"
        }, {
          default: withCtx(() => [
            unref(selectedDevice) === "" ? (openBlock(), createElementBlock("div", _hoisted_4$g, [
              createBaseVNode("div", _hoisted_5$e, [
                _hoisted_6$b,
                createBaseVNode("div", _hoisted_7$a, [
                  createVNode(_component_el_input, {
                    modelValue: unref(searchInput),
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(searchInput) ? searchInput.value = $event : null),
                    style: { "width": "240px" },
                    placeholder: "从模板库搜索",
                    "prefix-icon": unref(search_default)
                  }, null, 8, ["modelValue", "prefix-icon"])
                ])
              ]),
              createBaseVNode("div", _hoisted_8$6, [
                (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainDevice), (device) => {
                  return openBlock(), createBlock(Card, {
                    key: device.code,
                    cardItem: device,
                    onItemClick: ($event) => handleDomainDeviceClick(device)
                  }, null, 8, ["cardItem", "onItemClick"]);
                }), 128))
              ])
            ])) : (openBlock(), createElementBlock("div", _hoisted_9$5, [
              createBaseVNode("div", _hoisted_10$2, [
                createVNode(_component_el_form, {
                  model: unref(deviceForm),
                  rules: unref(rules),
                  ref_key: "deviceFormRef",
                  ref: deviceFormRef,
                  "label-width": "auto",
                  "label-position": "left",
                  style: { "max-width": "500px" }
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, {
                      label: "设备码",
                      prop: "deviceCode"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(deviceForm).deviceCode,
                          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(deviceForm).deviceCode = $event),
                          placeholder: "请输入"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "设备名",
                      prop: "deviceName"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(deviceForm).deviceName,
                          "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => unref(deviceForm).deviceName = $event),
                          placeholder: "请输入"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "设备类型",
                      prop: "deviceType.code"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: unref(deviceForm).deviceType.code,
                          "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => unref(deviceForm).deviceType.code = $event),
                          disabled: ""
                        }, {
                          default: withCtx(() => [
                            (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainDevice), (device) => {
                              return openBlock(), createBlock(_component_el_option, {
                                key: device.code,
                                label: device.name,
                                value: device.code
                              }, null, 8, ["label", "value"]);
                            }), 128))
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "设备品牌/厂商",
                      prop: "deviceService.code"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: unref(deviceForm).deviceService.code,
                          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => unref(deviceForm).deviceService.code = $event),
                          placeholder: "请选择品牌/厂商"
                        }, {
                          default: withCtx(() => [
                            (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainDevice).find((v) => v.code === unref(deviceForm).deviceType.code)?.services ?? [], (service) => {
                              return openBlock(), createBlock(_component_el_option, {
                                key: service.code,
                                label: service.name,
                                value: service.code
                              }, null, 8, ["label", "value"]);
                            }), 128))
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "协议",
                      prop: "protocol"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: unref(deviceForm).protocol,
                          "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => unref(deviceForm).protocol = $event),
                          placeholder: "请选择协议"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "HTTP",
                              value: "HTTP"
                            }),
                            createVNode(_component_el_option, {
                              label: "TCP/IP",
                              value: "TCP/IP"
                            })
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "主机名/IP地址",
                      prop: "host"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(deviceForm).host,
                          "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => unref(deviceForm).host = $event),
                          placeholder: "请输入"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, {
                      label: "端口号",
                      prop: "port"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: unref(deviceForm).port,
                          "onUpdate:modelValue": _cache[8] || (_cache[8] = ($event) => unref(deviceForm).port = $event),
                          type: "number",
                          placeholder: "请输入"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, null, {
                      default: withCtx(() => [
                        createVNode(_component_el_button, {
                          type: "primary",
                          onClick: _cache[9] || (_cache[9] = ($event) => submitForm(unref(deviceFormRef)))
                        }, {
                          default: withCtx(() => [
                            createTextVNode(" 确认 ")
                          ]),
                          _: 1
                        }),
                        createVNode(_component_el_button, { onClick: handleReturnDomainDevice }, {
                          default: withCtx(() => [
                            createTextVNode("返回")
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model", "rules"])
              ])
            ]))
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const _withScopeId$8 = (n) => (pushScopeId("data-v-fc8a6e66"), n = n(), popScopeId(), n);
const _hoisted_1$n = { style: { "padding": "20px" } };
const _hoisted_2$j = { class: "scenario-map" };
const _hoisted_3$h = /* @__PURE__ */ _withScopeId$8(() => /* @__PURE__ */ createBaseVNode("div", {
  class: "scenario-title",
  id: "场景区域"
}, "场景区域", -1));
const _hoisted_4$f = { class: "scenario-resource" };
const _hoisted_5$d = /* @__PURE__ */ _withScopeId$8(() => /* @__PURE__ */ createBaseVNode("div", {
  class: "scenario-title",
  id: "场景资源"
}, "场景资源", -1));
const _sfc_main$o = /* @__PURE__ */ defineComponent({
  __name: "ScenarioDetail",
  setup(__props) {
    const anchorRef = ref(null);
    const state = reactive({
      scenarioId: "",
      scenarioName: ""
    });
    const { scenarioId, scenarioName } = toRefs(state);
    const router = useRouter();
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.scenarioId === "string") {
        scenarioId.value = router.currentRoute.value.query.scenarioId || "";
      }
      if (typeof router.currentRoute.value.query.scenarioName === "string") {
        scenarioName.value = router.currentRoute.value.query.scenarioName || "";
      }
    });
    return (_ctx, _cache) => {
      const _component_el_breadcrumb_item = ElBreadcrumbItem;
      const _component_el_breadcrumb = ElBreadcrumb;
      const _component_el_col = ElCol;
      const _component_el_anchor_link = ElAnchorLink;
      const _component_el_anchor = ElAnchor;
      const _component_el_row = ElRow;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(_component_el_breadcrumb, {
          separator: "/",
          class: "scenario-breadcrumb"
        }, {
          default: withCtx(() => [
            createVNode(_component_el_breadcrumb_item, {
              to: { path: "/developer/workspace", query: { active: "scenario" } },
              class: "scenario-breadcrumb-item"
            }, {
              default: withCtx(() => [
                createTextVNode("我的场景")
              ]),
              _: 1
            }),
            createVNode(_component_el_breadcrumb_item, { class: "scenario-breadcrumb-item" }, {
              default: withCtx(() => [
                createTextVNode(toDisplayString(unref(scenarioName) || "--场景"), 1)
              ]),
              _: 1
            })
          ]),
          _: 1
        }),
        createVNode(_component_el_row, null, {
          default: withCtx(() => [
            createVNode(_component_el_col, { span: 18 }, {
              default: withCtx(() => [
                createBaseVNode("div", _hoisted_1$n, [
                  createBaseVNode("div", _hoisted_2$j, [
                    _hoisted_3$h,
                    createBaseVNode("div", null, [
                      createVNode(_sfc_main$q, {
                        scenarioId: unref(scenarioId),
                        scenarioName: unref(scenarioName)
                      }, null, 8, ["scenarioId", "scenarioName"])
                    ])
                  ]),
                  createBaseVNode("div", _hoisted_4$f, [
                    _hoisted_5$d,
                    createBaseVNode("div", null, [
                      createVNode(_sfc_main$p, {
                        scenarioId: unref(scenarioId),
                        scenarioName: unref(scenarioName)
                      }, null, 8, ["scenarioId", "scenarioName"])
                    ])
                  ])
                ])
              ]),
              _: 1
            }),
            createVNode(_component_el_col, { span: 6 }, {
              default: withCtx(() => [
                createVNode(_component_el_anchor, {
                  container: unref(anchorRef),
                  direction: "vertical",
                  type: "default",
                  offset: 30,
                  class: "anchor"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_anchor_link, {
                      href: "#场景区域",
                      title: "场景区域"
                    }, {
                      "sub-link": withCtx(() => [
                        createVNode(_component_el_anchor_link, {
                          href: "#场景地图",
                          title: "场景地图"
                        }),
                        createVNode(_component_el_anchor_link, {
                          href: "#区域列表",
                          title: "区域列表"
                        })
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_anchor_link, {
                      href: "#场景资源",
                      title: "场景资源"
                    }, {
                      "sub-link": withCtx(() => [
                        createVNode(_component_el_anchor_link, {
                          href: "#设备",
                          title: "设备"
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["container"])
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ], 64);
    };
  }
});/* unplugin-vue-components disabled *//* unplugin-vue-components disabled */const ScenarioDetail = /* @__PURE__ */ _export_sfc(_sfc_main$o, [["__scopeId", "data-v-fc8a6e66"]]);var define_import_meta_env_default$3 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$m = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$i = /* @__PURE__ */ createBaseVNode("div", { id: "区域字段列表" }, "区域字段列表", -1);
const _hoisted_3$g = { class: "domain-content" };
const _hoisted_4$e = { style: { "padding": "20px" } };
const _sfc_main$n = /* @__PURE__ */ defineComponent({
  __name: "DomainArea",
  props: {
    domainId: String,
    domainName: String
  },
  setup(__props) {
    const rules = reactive({
      fieldNumber: [
        { required: true, message: "请输入字段号", trigger: "blur" }
      ],
      fieldName: [
        { required: true, message: "请输入字段名称", trigger: "blur" }
      ],
      fieldType: [
        { required: true, message: "请输入字段数据类型", trigger: "blur" }
      ]
    });
    const fieldFormRef = ref();
    const FieldForm = reactive({
      fieldNumber: "",
      fieldName: "",
      fieldType: ""
    });
    const state = reactive({
      header: [
        {
          code: "code",
          name: "字段号",
          type: "String"
        },
        {
          code: "name",
          name: "字段名称",
          type: "String"
        },
        {
          code: "type",
          name: "数据类型",
          type: "String"
        }
      ],
      data: [],
      dialogVisible: false
    });
    const { header, data, dialogVisible } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default$3.VITE_MODE === "mock") {
        data.value = [{
          code: "floor",
          name: "楼层",
          type: "String"
        }, {
          code: "description",
          name: "描述",
          type: "String"
        }, {
          code: "planPath",
          name: "平面图",
          type: "Image"
        }];
      } else {
        getDomainData();
      }
    });
    const resetForm = () => {
      if (fieldFormRef) {
        fieldFormRef.value.resetFields();
      }
      return;
    };
    const submitForm = async (formEl) => {
      if (!formEl)
        return;
      await formEl.validate((valid, fields) => {
        if (valid) {
          console.log("submit!", FieldForm);
        } else {
          console.log("error submit!", fields);
        }
      });
    };
    const getDomainData = () => {
      getDomainJson().then((res) => {
        if (res.status === 200) {
          data.value = res.data.domainField;
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$m, [
          _hoisted_2$i,
          createVNode(_component_el_button, {
            onClick: _cache[0] || (_cache[0] = ($event) => dialogVisible.value = true),
            type: "primary",
            style: { "margin-left": "auto" }
          }, {
            default: withCtx(() => [
              createTextVNode("新增字段")
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_3$g, [
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data),
            canChoose: true
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "新增字段",
          width: "50%"
        }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_4$e, [
              createVNode(_component_el_form, {
                rules: unref(rules),
                model: unref(FieldForm),
                ref_key: "fieldFormRef",
                ref: fieldFormRef,
                "label-width": "auto",
                "label-position": "left",
                style: { "max-width": "500px" }
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_form_item, {
                    label: "字段号",
                    prop: "fieldNumber"
                  }, {
                    default: withCtx(() => [
                      createVNode(_component_el_input, {
                        modelValue: unref(FieldForm).fieldNumber,
                        "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(FieldForm).fieldNumber = $event),
                        placeholder: "请输入"
                      }, null, 8, ["modelValue"])
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_form_item, {
                    label: "字段名称",
                    prop: "fieldName"
                  }, {
                    default: withCtx(() => [
                      createVNode(_component_el_input, {
                        modelValue: unref(FieldForm).fieldName,
                        "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(FieldForm).fieldName = $event),
                        placeholder: "请输入"
                      }, null, 8, ["modelValue"])
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_form_item, {
                    label: "数据类型",
                    prop: "fieldType"
                  }, {
                    default: withCtx(() => [
                      createVNode(_component_el_input, {
                        modelValue: unref(FieldForm).fieldType,
                        "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => unref(FieldForm).fieldType = $event),
                        placeholder: "请输入"
                      }, null, 8, ["modelValue"])
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_form_item, null, {
                    default: withCtx(() => [
                      createVNode(_component_el_button, {
                        type: "primary",
                        onClick: _cache[4] || (_cache[4] = ($event) => submitForm(unref(fieldFormRef)))
                      }, {
                        default: withCtx(() => [
                          createTextVNode(" 确认 ")
                        ]),
                        _: 1
                      }),
                      createVNode(_component_el_button, { onClick: resetForm }, {
                        default: withCtx(() => [
                          createTextVNode("重置")
                        ]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["rules", "model"])
            ])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});var define_import_meta_env_default$2 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$l = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$h = { class: "domain-content" };
const _hoisted_3$f = { style: { "display": "flex", "justify-content": "space-between", "font-size": "16px", "margin-bottom": "20px", "margin-top": "10px" } };
const _hoisted_4$d = /* @__PURE__ */ createBaseVNode("div", null, "模板库", -1);
const _hoisted_5$c = { style: { "color": "#50a5fb" } };
const _hoisted_6$a = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _hoisted_7$9 = { style: { "margin-top": "20px", "display": "flex", "justify-content": "end" } };
const _sfc_main$m = /* @__PURE__ */ defineComponent({
  __name: "ResourceUI",
  props: {
    domainId: String,
    domainName: String
  },
  setup(__props) {
    const state = reactive({
      header: [
        {
          code: "code",
          name: "组件号",
          type: "Int"
        },
        {
          code: "name",
          name: "组件名称",
          type: "String"
        },
        {
          code: "imgUrl",
          name: "组件图标",
          type: "Image"
        }
      ],
      data: [],
      dialogVisible: false,
      searchInput: "",
      domainUI: [],
      selectedUIList: []
    });
    const { header, data, dialogVisible, searchInput, domainUI, selectedUIList } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default$2.VITE_MODE === "mock") {
        data.value = [
          {
            code: "001",
            name: "场景化导航",
            imgUrl: getAssetsFile("images/guide.svg")
          },
          {
            code: "002",
            name: "柱状图",
            imgUrl: getAssetsFile("images/bar.svg")
          }
        ];
      } else {
        getDomainData();
      }
      domainUI.value = [
        {
          code: "001",
          name: "场景化导航",
          isSelected: false,
          imageUrl: getAssetsFile("images/guide.svg")
        },
        {
          code: "002",
          name: "柱状图",
          isSelected: false,
          imageUrl: getAssetsFile("images/bar.svg")
        }
      ];
    });
    const getDomainData = () => {
      getDomainComponent("UI").then((res) => {
        if (res.status === 200) {
          console.log(res.data);
          data.value = res.data.componentAbout.map((v) => {
            const imgPath = "icon/" + v.imgPath;
            return {
              code: v.componentId,
              name: v.componentName,
              imgUrl: getAssetsFile(imgPath),
              isSelected: false
            };
          });
        }
      });
    };
    const updateIsSelected = (index, value) => {
      domainUI.value[index].isSelected = value;
    };
    const clearUIComponent = () => {
      domainUI.value.forEach((device) => {
        device.isSelected = false;
      });
      selectedUIList.value = [];
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$l, [
          createVNode(_component_el_button, {
            type: "primary",
            style: { "margin-left": "auto" },
            onClick: _cache[0] || (_cache[0] = ($event) => dialogVisible.value = true)
          }, {
            default: withCtx(() => [
              createTextVNode("添加UI组件")
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_2$h, [
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data),
            canChoose: true
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "添加UI组件",
          width: "50%",
          onClose: clearUIComponent
        }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_3$f, [
              _hoisted_4$d,
              createBaseVNode("div", _hoisted_5$c, [
                createVNode(_component_el_input, {
                  modelValue: unref(searchInput),
                  "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(searchInput) ? searchInput.value = $event : null),
                  style: { "width": "240px" },
                  placeholder: "搜索",
                  "prefix-icon": unref(search_default)
                }, null, 8, ["modelValue", "prefix-icon"])
              ])
            ]),
            createBaseVNode("div", _hoisted_6$a, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainUI), (ui, index) => {
                return openBlock(), createBlock(Card, {
                  key: ui.code,
                  cardItem: ui,
                  canSelect: "",
                  "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
                }, null, 8, ["cardItem", "onUpdate:isSelected"]);
              }), 128))
            ]),
            createBaseVNode("div", _hoisted_7$9, [
              createVNode(_component_el_button, { type: "primary" }, {
                default: withCtx(() => [
                  createTextVNode("确定")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, { onClick: clearUIComponent }, {
                default: withCtx(() => [
                  createTextVNode("清空")
                ]),
                _: 1
              })
            ])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});var define_import_meta_env_default$1 = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$k = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$g = { class: "domain-content" };
const _hoisted_3$e = { style: { "display": "flex", "justify-content": "space-between", "font-size": "16px", "margin-bottom": "20px", "margin-top": "10px" } };
const _hoisted_4$c = /* @__PURE__ */ createBaseVNode("div", null, "模板库", -1);
const _hoisted_5$b = { style: { "color": "#50a5fb" } };
const _hoisted_6$9 = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _hoisted_7$8 = { style: { "margin-top": "20px", "display": "flex", "justify-content": "end" } };
const _sfc_main$l = /* @__PURE__ */ defineComponent({
  __name: "ResourceProcess",
  props: {
    domainId: String,
    domainName: String
  },
  setup(__props) {
    const state = reactive({
      header: [
        {
          code: "number",
          name: "流程号",
          type: "String"
        },
        {
          code: "name",
          name: "流程名称",
          type: "String"
        },
        {
          code: "brief",
          name: "流程简介",
          type: "String"
        }
      ],
      data: [],
      searchInput: "",
      dialogVisible: false,
      domainProcess: [],
      selectedProcessList: []
    });
    const { header, data, searchInput, dialogVisible, selectedProcessList, domainProcess } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default$1.VITE_MODE === "mock") {
        data.value = [{
          number: "001",
          name: "预约流程",
          brief: "此流程用于各类预约系统，可以实现预约时间选择、预约队列管理等"
        }];
      } else {
        getDomainData();
      }
      domainProcess.value = [
        {
          code: "001",
          name: "预约流程",
          isSelected: false,
          imageUrl: new URL("/index/img/logo.png", import.meta.url).href
        },
        {
          code: "002",
          name: "审批流程",
          isSelected: false,
          imageUrl: new URL("/index/img/logo.png", import.meta.url).href
        }
      ];
    });
    const getDomainData = () => {
      getDomainComponent("Process").then((res) => {
        if (res.status === 200) {
          console.log(res.data);
          data.value = res.data.componentAbout.map((v) => {
            return {
              number: v.componentId,
              name: v.componentName,
              brief: v.brief
            };
          });
        }
      });
    };
    const updateIsSelected = (index, value) => {
      domainProcess.value[index].isSelected = value;
    };
    const clearProcess = () => {
      domainProcess.value.forEach((device) => {
        device.isSelected = false;
      });
      selectedProcessList.value = [];
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$k, [
          createVNode(_component_el_button, {
            type: "primary",
            onClick: _cache[0] || (_cache[0] = ($event) => dialogVisible.value = true),
            style: { "margin-left": "auto" }
          }, {
            default: withCtx(() => [
              createTextVNode("添加流程类型")
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_2$g, [
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data),
            canChoose: true
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "添加流程类型",
          width: "50%",
          onClose: clearProcess
        }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_3$e, [
              _hoisted_4$c,
              createBaseVNode("div", _hoisted_5$b, [
                createVNode(_component_el_input, {
                  modelValue: unref(searchInput),
                  "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(searchInput) ? searchInput.value = $event : null),
                  style: { "width": "240px" },
                  placeholder: "搜索",
                  "prefix-icon": unref(search_default)
                }, null, 8, ["modelValue", "prefix-icon"])
              ])
            ]),
            createBaseVNode("div", _hoisted_6$9, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainProcess), (process, index) => {
                return openBlock(), createBlock(Card, {
                  key: process.code,
                  cardItem: process,
                  canSelect: "",
                  "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
                }, null, 8, ["cardItem", "onUpdate:isSelected"]);
              }), 128))
            ]),
            createBaseVNode("div", _hoisted_7$8, [
              createVNode(_component_el_button, { type: "primary" }, {
                default: withCtx(() => [
                  createTextVNode("确定")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, { onClick: clearProcess }, {
                default: withCtx(() => [
                  createTextVNode("清空")
                ]),
                _: 1
              })
            ])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const _hoisted_1$j = { class: "domain-content" };
const _sfc_main$k = /* @__PURE__ */ defineComponent({
  __name: "DomainResource",
  props: {
    domainId: String,
    domainName: String
  },
  setup(__props) {
    return (_ctx, _cache) => {
      const _component_el_tab_pane = ElTabPane;
      const _component_el_tabs = ElTabs;
      return openBlock(), createElementBlock("div", _hoisted_1$j, [
        createVNode(_component_el_tabs, { type: "border-card" }, {
          default: withCtx(() => [
            createVNode(_component_el_tab_pane, { label: "UI组件" }, {
              default: withCtx(() => [
                createVNode(_sfc_main$m, {
                  domainId: __props.domainId,
                  domainName: __props.domainName
                }, null, 8, ["domainId", "domainName"])
              ]),
              _: 1
            }),
            createVNode(_component_el_tab_pane, { label: "流程" }, {
              default: withCtx(() => [
                createVNode(_sfc_main$l, {
                  domainId: __props.domainId,
                  domainName: __props.domainName
                }, null, 8, ["domainId", "domainName"])
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ]);
    };
  }
});var define_import_meta_env_default = { VITE_ENV: "production", VITE_BASE_PATH: "http://139.196.147.52:8080", VITE_BASE_API: "/api", BASE_URL: "/", MODE: "production", DEV: false, PROD: true, SSR: false };
const _hoisted_1$i = {
  class: "domain-subtitle",
  style: { "display": "flex", "justify-content": "space-between" }
};
const _hoisted_2$f = { class: "domain-content" };
const _hoisted_3$d = { style: { "display": "flex", "justify-content": "space-between", "font-size": "16px", "margin-bottom": "20px", "margin-top": "10px" } };
const _hoisted_4$b = /* @__PURE__ */ createBaseVNode("div", null, "模板库", -1);
const _hoisted_5$a = { style: { "color": "#50a5fb" } };
const _hoisted_6$8 = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "20px" } };
const _hoisted_7$7 = { style: { "margin-top": "20px", "display": "flex", "justify-content": "end" } };
const _sfc_main$j = /* @__PURE__ */ defineComponent({
  __name: "ResourceDevice",
  props: {
    domainId: String,
    domainName: String
  },
  setup(__props) {
    const state = reactive({
      header: [
        {
          code: "ID",
          name: "类型ID",
          type: "String"
        },
        {
          code: "name",
          name: "设备名称",
          type: "String"
        }
      ],
      data: [],
      dialogVisible: false,
      searchInput: "",
      domainDevice: [],
      selectedDeviceList: []
    });
    const { header, data, dialogVisible, searchInput, domainDevice, selectedDeviceList } = toRefs(state);
    onMounted(() => {
      if (define_import_meta_env_default.VITE_MODE === "mock") {
        data.value = [
          {
            ID: "CoffeeMaker",
            name: "咖啡机"
          },
          {
            ID: "AirConditioner",
            name: "空调"
          }
        ];
        domainDevice.value = [
          {
            code: "CoffeeMaker",
            name: "咖啡机器人",
            isSelected: false,
            imageUrl: getAssetsFile("logo.png")
          },
          {
            code: "AirConditioner",
            name: "空调",
            isSelected: false,
            imageUrl: getAssetsFile("logo.png")
          },
          {
            code: "SmokeDetector",
            name: "烟感器",
            isSelected: false,
            imageUrl: getAssetsFile("logo.png")
          }
        ];
      } else {
        getDomainData();
      }
    });
    const openDialog = () => {
      dialogVisible.value = true;
      getDomainData();
    };
    const updateIsSelected = (index, value) => {
      domainDevice.value[index].isSelected = value;
    };
    const SubmitDevice = () => {
      const selectedDevices = domainDevice.value.filter((device) => device.isSelected);
      if (selectedDevices.length === 0) {
        ElMessage.warning("请先选择设备！");
        return;
      }
      const deviceCodes = selectedDevices.map((device) => device.code);
      console.log(selectedDevices);
      uploadDomainBindingData({ selectedCodes: deviceCodes }, "Device", "SmartBuilding").then(async (res) => {
        if (res.status === 200) {
          console.log("Binding successful", res.data);
          ElMessage.success("组件绑定成功");
          await getDomainData();
        }
      });
      dialogVisible.value = false;
    };
    const clearDevice = () => {
      domainDevice.value.forEach((device) => {
        device.isSelected = false;
      });
      selectedDeviceList.value = [];
    };
    const getDomainData = () => {
      loadDomainBindingData("Device", "SmartBuilding").then((res) => {
        if (res.status === 200) {
          data.value = res.data.map((v) => {
            return {
              ID: v.deviceTypeCode,
              name: v.deviceTypeName
            };
          });
        }
      });
      domainDevice.value = [];
      loadDoaminComponentData("Device").then((res) => {
        if (res.status === 200) {
          const dataArray = res.data;
          dataArray.forEach((device) => {
            const isDeviceInData = data.value.some((item) => item.ID === device.deviceTypeCode);
            if (!isDeviceInData) {
              const newDevice = {
                code: device.deviceTypeCode,
                name: device.deviceTypeName,
                isSelected: false,
                imageUrl: getAssetsFile("device/" + device.deviceTypeCode + ".png")
              };
              domainDevice.value.push(newDevice);
            }
          });
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createBaseVNode("div", _hoisted_1$i, [
          createVNode(_component_el_button, {
            type: "primary",
            onClick: openDialog,
            style: { "margin-left": "auto" }
          }, {
            default: withCtx(() => [
              createTextVNode("添加设备类型")
            ]),
            _: 1
          })
        ]),
        createBaseVNode("div", _hoisted_2$f, [
          createVNode(_sfc_main$v, {
            header: unref(header),
            data: unref(data),
            canChoose: true
          }, null, 8, ["header", "data"])
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "添加设备类型",
          width: "50%",
          onClose: clearDevice
        }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_3$d, [
              _hoisted_4$b,
              createBaseVNode("div", _hoisted_5$a, [
                createVNode(_component_el_input, {
                  modelValue: unref(searchInput),
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => isRef(searchInput) ? searchInput.value = $event : null),
                  style: { "width": "240px" },
                  placeholder: "搜索",
                  "prefix-icon": unref(search_default)
                }, null, 8, ["modelValue", "prefix-icon"])
              ])
            ]),
            createBaseVNode("div", _hoisted_6$8, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(unref(domainDevice), (device, index) => {
                return openBlock(), createBlock(Card, {
                  key: device.code,
                  cardItem: device,
                  canSelect: "",
                  "onUpdate:isSelected": ($event) => updateIsSelected(index, $event)
                }, null, 8, ["cardItem", "onUpdate:isSelected"]);
              }), 128))
            ]),
            createBaseVNode("div", _hoisted_7$7, [
              createVNode(_component_el_button, {
                type: "primary",
                onClick: SubmitDevice
              }, {
                default: withCtx(() => [
                  createTextVNode("确定")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, { onClick: clearDevice }, {
                default: withCtx(() => [
                  createTextVNode("清空")
                ]),
                _: 1
              })
            ])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const _withScopeId$7 = (n) => (pushScopeId("data-v-c35ec6de"), n = n(), popScopeId(), n);
const _hoisted_1$h = { style: { "padding": "20px" } };
const _hoisted_2$e = { class: "domain-setting" };
const _hoisted_3$c = /* @__PURE__ */ _withScopeId$7(() => /* @__PURE__ */ createBaseVNode("div", {
  class: "domain-title",
  id: "领域设置"
}, "领域设置", -1));
const _hoisted_4$a = { style: { "margin-top": "30px" } };
const _hoisted_5$9 = { class: "domain-resource" };
const _hoisted_6$7 = /* @__PURE__ */ _withScopeId$7(() => /* @__PURE__ */ createBaseVNode("div", {
  class: "domain-title",
  id: "领域设备类型"
}, "设备类型", -1));
const _hoisted_7$6 = { style: { "margin-top": "30px" } };
const _hoisted_8$5 = { class: "domain-resource" };
const _hoisted_9$4 = /* @__PURE__ */ _withScopeId$7(() => /* @__PURE__ */ createBaseVNode("div", {
  class: "domain-title",
  id: "领域模板管理"
}, "模板管理", -1));
const _hoisted_10$1 = { style: { "margin-top": "30px" } };
const _sfc_main$i = /* @__PURE__ */ defineComponent({
  __name: "DomainDetail",
  setup(__props) {
    const anchorRef = ref(null);
    const state = reactive({
      domainId: "",
      domainName: ""
    });
    const { domainId, domainName } = toRefs(state);
    const router = useRouter();
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.domainId === "string") {
        domainId.value = router.currentRoute.value.query.domainId || "";
      }
      if (typeof router.currentRoute.value.query.domainName === "string") {
        domainName.value = router.currentRoute.value.query.domainName || "";
      }
    });
    return (_ctx, _cache) => {
      const _component_el_breadcrumb_item = ElBreadcrumbItem;
      const _component_el_breadcrumb = ElBreadcrumb;
      const _component_el_col = ElCol;
      const _component_el_anchor_link = ElAnchorLink;
      const _component_el_anchor = ElAnchor;
      const _component_el_row = ElRow;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(_component_el_breadcrumb, {
          separator: "/",
          class: "scenario-breadcrumb"
        }, {
          default: withCtx(() => [
            createVNode(_component_el_breadcrumb_item, {
              to: { path: "/developer/workspace", query: { active: "domain" } },
              class: "domain-breadcrumb-item"
            }, {
              default: withCtx(() => [
                createTextVNode("我的领域")
              ]),
              _: 1
            }),
            createVNode(_component_el_breadcrumb_item, { class: "domain-breadcrumb-item" }, {
              default: withCtx(() => [
                createTextVNode(toDisplayString(unref(domainName) || "--领域"), 1)
              ]),
              _: 1
            })
          ]),
          _: 1
        }),
        createVNode(_component_el_row, null, {
          default: withCtx(() => [
            createVNode(_component_el_col, { span: 18 }, {
              default: withCtx(() => [
                createBaseVNode("div", _hoisted_1$h, [
                  createBaseVNode("div", _hoisted_2$e, [
                    _hoisted_3$c,
                    createBaseVNode("div", _hoisted_4$a, [
                      createVNode(_sfc_main$n, {
                        domainId: unref(domainId),
                        domainName: unref(domainName)
                      }, null, 8, ["domainId", "domainName"])
                    ])
                  ]),
                  createBaseVNode("div", _hoisted_5$9, [
                    _hoisted_6$7,
                    createBaseVNode("div", _hoisted_7$6, [
                      createVNode(_sfc_main$j, {
                        domainId: unref(domainId),
                        domainName: unref(domainName)
                      }, null, 8, ["domainId", "domainName"])
                    ])
                  ]),
                  createBaseVNode("div", _hoisted_8$5, [
                    _hoisted_9$4,
                    createBaseVNode("div", _hoisted_10$1, [
                      createVNode(_sfc_main$k, {
                        domainId: unref(domainId),
                        domainName: unref(domainName)
                      }, null, 8, ["domainId", "domainName"])
                    ])
                  ])
                ])
              ]),
              _: 1
            }),
            createVNode(_component_el_col, { span: 6 }, {
              default: withCtx(() => [
                createVNode(_component_el_anchor, {
                  container: unref(anchorRef),
                  offset: 30,
                  direction: "vertical",
                  marker: "",
                  type: "default",
                  class: "anchor"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_anchor_link, {
                      href: "#领域设置",
                      title: "领域设置"
                    }),
                    createVNode(_component_el_anchor_link, {
                      href: "#领域设备类型",
                      title: "领域设备类型"
                    }),
                    createVNode(_component_el_anchor_link, {
                      href: "#领域模板管理",
                      title: "领域模板管理"
                    })
                  ]),
                  _: 1
                }, 8, ["container"])
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ], 64);
    };
  }
});/* unplugin-vue-components disabled *//* unplugin-vue-components disabled */const DomainDetail = /* @__PURE__ */ _export_sfc(_sfc_main$i, [["__scopeId", "data-v-c35ec6de"]]);const _hoisted_1$g = { class: "header" };
const _hoisted_2$d = ["src"];
const _hoisted_3$b = /* @__PURE__ */ createBaseVNode("div", { style: { "line-height": "32px", "padding-left": "10px" } }, "面向场景计算的低代码设计器", -1);
const _sfc_main$h = /* @__PURE__ */ defineComponent({
  __name: "DesignHeader",
  setup(__props) {
    const router = useRouter();
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock("div", _hoisted_1$g, [
        createBaseVNode("div", {
          class: "header-title",
          style: { "cursor": "pointer" },
          onClick: _cache[0] || (_cache[0] = ($event) => unref(router).push({ path: "/" }))
        }, [
          createBaseVNode("img", {
            width: "30",
            height: "30",
            title: "低代码",
            src: unref(logo)
          }, null, 8, _hoisted_2$d),
          _hoisted_3$b
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled *//* unplugin-vue-components disabled */
const _sfc_main$g = {
  name: "MdiShieldBug"
};

const _withScopeId$6 = n => (pushScopeId("data-v-fe4ae48a"),n=n(),popScopeId(),n);
const _hoisted_1$f = {
  xmlns: "http://www.w3.org/2000/svg",
  width: "1.2em",
  height: "1.2em",
  viewBox: "0 0 24 24",
  class: "svg-icon"
};
const _hoisted_2$c = /*#__PURE__*/ _withScopeId$6(() => /*#__PURE__*/createBaseVNode("path", {
  fill: "currentColor",
  d: "M11 13h2v1h-2zm10-8v6c0 5.5-3.8 10.7-9 12c-5.2-1.3-9-6.5-9-12V5l9-4zm-4 5h-2.2c-.2-.6-.6-1.1-1.1-1.5l1.2-1.2l-.7-.7L12.8 8H12c-.2 0-.5 0-.7.1L9.9 6.6l-.8.8l1.2 1.2c-.5.3-.9.8-1.1 1.4H7v1h2v1H7v1h2v1H7v1h2.2c.4 1.2 1.5 2 2.8 2s2.4-.8 2.8-2H17v-1h-2v-1h2v-1h-2v-1h2zm-6 2h2v-1h-2z"
}, null, -1));
const _hoisted_3$a = [
  _hoisted_2$c
];

function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return (openBlock(), createElementBlock("svg", _hoisted_1$f, _hoisted_3$a))
}
const MdiShieldBug = /*#__PURE__*/_export_sfc(_sfc_main$g, [['render',_sfc_render],['__scopeId',"data-v-fe4ae48a"]]);const _withScopeId$5 = (n) => (pushScopeId("data-v-402c59a3"), n = n(), popScopeId(), n);
const _hoisted_1$e = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "应用信息", -1));
const _hoisted_2$b = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "业务流程", -1));
const _hoisted_3$9 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "设备逻辑", -1));
const _hoisted_4$9 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "页面管理", -1));
const _hoisted_5$8 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "菜单管理", -1));
const _hoisted_6$6 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "页面流管理", -1));
const _hoisted_7$5 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "全局变量管理", -1));
const _hoisted_8$4 = /* @__PURE__ */ _withScopeId$5(() => /* @__PURE__ */ createBaseVNode("div", { class: "menu-item" }, "质量保障", -1));
const _sfc_main$f = /* @__PURE__ */ defineComponent({
  __name: "DesignAsideBar",
  props: {
    applicationId: String,
    applicationName: String
  },
  setup(__props) {
    const props = __props;
    const state = reactive({
      selectedItem: "",
      items: [
        {
          index: "index",
          name: "设计首页",
          route: "/design/index"
        },
        {
          index: "process",
          name: "流程目录",
          route: "/design/process"
        },
        {
          index: "page",
          name: "页面目录",
          route: "/design/page"
        },
        {
          index: "quality_assurance",
          name: "质量保障",
          route: "/design/quality_assurance"
        },
        {
          index: "device_logic",
          name: "设备逻辑",
          route: "/design/device_logic"
        },
        {
          index: "menu",
          name: "菜单管理",
          route: "/design/menu"
        },
        {
          index: "page_flow",
          name: "页面流管理",
          route: "/design/page_flow"
        },
        {
          index: "global_variable",
          name: "全局变量管理",
          route: "/design/global_variable"
        }
      ]
    });
    const { selectedItem, items } = toRefs(state);
    watchEffect(() => {
      const path = router.currentRoute.value.path;
      const matchingItemData = items.value.find((item) => item.route === path);
      if (matchingItemData) {
        selectedItem.value = matchingItemData.index;
      } else if (path.includes("/design/edit/process")) {
        selectedItem.value = "process";
      } else {
        selectedItem.value = "";
      }
    });
    const handleMenuSelect = (key, _keyPath) => {
      selectedItem.value = key;
      const selectedItemData = items.value.find((item) => item.index === key);
      if (selectedItemData) {
        router.push({ path: selectedItemData.route, query: { applicationId: props.applicationId, applicationName: props.applicationName } });
      }
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_menu_item = ElMenuItem;
      const _component_el_menu = ElMenu;
      return openBlock(), createBlock(_component_el_menu, {
        "default-active": unref(selectedItem),
        class: "nav-menu",
        onSelect: handleMenuSelect
      }, {
        default: withCtx(() => [
          createVNode(_component_el_menu_item, {
            index: "index",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(house_default))
                ]),
                _: 1
              }),
              _hoisted_1$e
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "process",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(connection_default))
                ]),
                _: 1
              }),
              _hoisted_2$b
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "device_logic",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(cpu_default))
                ]),
                _: 1
              }),
              _hoisted_3$9
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "page",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(document_default))
                ]),
                _: 1
              }),
              _hoisted_4$9
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "menu",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(more_default))
                ]),
                _: 1
              }),
              _hoisted_5$8
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "page_flow",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(share_default))
                ]),
                _: 1
              }),
              _hoisted_6$6
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "global_variable",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(_component_el_icon, null, {
                default: withCtx(() => [
                  createVNode(unref(eleme_default))
                ]),
                _: 1
              }),
              _hoisted_7$5
            ]),
            _: 1
          }),
          createVNode(_component_el_menu_item, {
            index: "quality_assurance",
            class: "nav-item"
          }, {
            title: withCtx(() => [
              createVNode(MdiShieldBug),
              _hoisted_8$4
            ]),
            _: 1
          })
        ]),
        _: 1
      }, 8, ["default-active"]);
    };
  }
});/* unplugin-vue-components disabled */const DesignAsideBar = /* @__PURE__ */ _export_sfc(_sfc_main$f, [["__scopeId", "data-v-402c59a3"]]);const _sfc_main$e = /* @__PURE__ */ defineComponent({
  __name: "ApplicationDetail",
  setup(__props) {
    const state = reactive({
      applicationId: "",
      applicationName: ""
    });
    const { applicationId, applicationName } = toRefs(state);
    const router = useRouter();
    const applicationStore = useApplicationStore();
    watchEffect(() => {
      const routerQuery = router.currentRoute.value.query;
      if (typeof routerQuery.applicationId === "string" && routerQuery.applicationId !== "") {
        applicationId.value = routerQuery.applicationId || "";
      } else {
        applicationId.value = applicationStore.getApplicationId;
      }
      if (typeof routerQuery.applicationName === "string" && routerQuery.applicationName !== "") {
        applicationName.value = routerQuery.applicationName || "";
      } else {
        applicationName.value = applicationStore.getApplicationName;
      }
    });
    return (_ctx, _cache) => {
      const _component_el_header = ElHeader;
      const _component_el_aside = ElAside;
      const _component_RouterView = resolveComponent("RouterView");
      const _component_el_main = ElMain;
      const _component_el_container = ElContainer;
      return openBlock(), createBlock(_component_el_container, { style: { "width": "100%", "height": "100%" } }, {
        default: withCtx(() => [
          createVNode(_component_el_header, { style: { "padding": "0" } }, {
            default: withCtx(() => [
              createVNode(_sfc_main$h, {
                applicationId: unref(applicationId),
                applicationName: unref(applicationName)
              }, null, 8, ["applicationId", "applicationName"])
            ]),
            _: 1
          }),
          createVNode(_component_el_container, null, {
            default: withCtx(() => [
              createVNode(_component_el_aside, { style: { "width": "200px" } }, {
                default: withCtx(() => [
                  createVNode(DesignAsideBar, {
                    applicationId: unref(applicationId),
                    applicationName: unref(applicationName)
                  }, null, 8, ["applicationId", "applicationName"])
                ]),
                _: 1
              }),
              createVNode(_component_el_main, { style: { "padding": "30px 50px 50px" } }, {
                default: withCtx(() => [
                  createVNode(_component_RouterView, null, {
                    default: withCtx((slotProps) => [
                      (openBlock(), createBlock(KeepAlive, null, [
                        (openBlock(), createBlock(resolveDynamicComponent(slotProps.Component)))
                      ], 1024))
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          })
        ]),
        _: 1
      });
    };
  }
});const _hoisted_1$d = { class: "header-box" };
const _hoisted_2$a = { class: "header-title" };
const _hoisted_3$8 = { style: { "display": "flex", "margin-right": "10px" } };
const _hoisted_4$8 = { style: { "margin-right": "10px" } };
const _sfc_main$d = /* @__PURE__ */ defineComponent({
  __name: "PageHeader",
  props: {
    title: String,
    buttonGroup: Array
  },
  emits: ["button-click"],
  setup(__props, { emit: __emit }) {
    const emit = __emit;
    const handleClick = (code) => {
      emit("button-click", code);
    };
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_ElButton = ElButton;
      return openBlock(), createElementBlock("div", _hoisted_1$d, [
        createBaseVNode("div", _hoisted_2$a, [
          createVNode(_component_el_icon, { class: "header-icon" }, {
            default: withCtx(() => [
              createVNode(unref(menu_default))
            ]),
            _: 1
          }),
          createBaseVNode("div", null, toDisplayString(__props.title), 1)
        ]),
        createBaseVNode("div", _hoisted_3$8, [
          (openBlock(true), createElementBlock(Fragment, null, renderList(__props.buttonGroup, (item) => {
            return openBlock(), createElementBlock("div", _hoisted_4$8, [
              createVNode(_component_ElButton, {
                type: item.type,
                onClick: ($event) => handleClick(item.code)
              }, {
                default: withCtx(() => [
                  createTextVNode(toDisplayString(item.name), 1)
                ]),
                _: 2
              }, 1032, ["type", "onClick"])
            ]);
          }), 256))
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled */const PageHeader = /* @__PURE__ */ _export_sfc(_sfc_main$d, [["__scopeId", "data-v-1d6cfeb1"]]);const _withScopeId$4 = (n) => (pushScopeId("data-v-134b9776"), n = n(), popScopeId(), n);
const _hoisted_1$c = { class: "body-box" };
const _hoisted_2$9 = /* @__PURE__ */ _withScopeId$4(() => /* @__PURE__ */ createBaseVNode("div", { class: "title-box" }, " 应用信息 ", -1));
const _sfc_main$c = /* @__PURE__ */ defineComponent({
  __name: "DesignIndex",
  setup(__props) {
    const state = reactive({
      applicationId: "",
      applicationName: "",
      applicationForm: {
        applicationName: "",
        description: "",
        applicationPath: "",
        scenario: "物理楼",
        domain: "智慧楼宇",
        isPublish: false
      },
      editStatus: false
    });
    const { applicationId, applicationName, applicationForm, editStatus } = toRefs(state);
    useRouter();
    const applicationStore = useApplicationStore();
    watchEffect(() => {
      applicationId.value = applicationStore.getApplicationId;
      applicationName.value = applicationStore.getApplicationName;
      applicationForm.value.applicationName = applicationName.value;
    });
    const buttonGroup = computed(() => {
      return applicationForm.value.isPublish ? [
        {
          code: "preview",
          name: "预览",
          type: "primary"
        },
        {
          code: "revert",
          name: "撤回",
          type: "warning"
        },
        {
          code: "code-view",
          name: "质量分析",
          type: "warning"
        },
        {
          code: "code-server",
          name: "代码编辑",
          type: "primary"
        }
      ] : [
        {
          code: "preview",
          name: "预览",
          type: "primary"
        },
        {
          code: "publish",
          name: "发布",
          type: "success"
        },
        {
          code: "code-view",
          name: "质量分析",
          type: "warning"
        },
        {
          code: "code-server",
          name: "代码编辑",
          type: "primary"
        }
      ];
    });
    const goToCodeServer = () => {
      window.open("http://139.196.147.52:5200/?folder=/home/coder/project/workplace", "_blank");
    };
    const handleHeaderButtonClick = (code) => {
      if (code === "code-server") {
        goToCodeServer();
      } else if (code === "publish") {
        applicationForm.value.isPublish = true;
      } else if (code === "revert") {
        applicationForm.value.isPublish = false;
      }
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_select = ElSelect;
      const _component_el_tag = ElTag;
      const _component_el_button = ElButton;
      const _component_el_form = ElForm;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——应用信息",
          "button-group": unref(buttonGroup),
          onButtonClick: handleHeaderButtonClick
        }, null, 8, ["title", "button-group"]),
        createBaseVNode("div", _hoisted_1$c, [
          _hoisted_2$9,
          createVNode(_component_el_form, {
            "label-width": "auto",
            form: unref(applicationForm),
            class: "application-form"
          }, {
            default: withCtx(() => [
              createVNode(_component_el_form_item, {
                label: "应用名称",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    modelValue: unref(applicationForm).applicationName,
                    "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => unref(applicationForm).applicationName = $event),
                    disabled: !unref(editStatus)
                  }, null, 8, ["modelValue", "disabled"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "应用介绍",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    type: "textarea",
                    modelValue: unref(applicationForm).description,
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(applicationForm).description = $event),
                    disabled: !unref(editStatus)
                  }, null, 8, ["modelValue", "disabled"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "应用路径",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    modelValue: unref(applicationForm).applicationPath,
                    "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(applicationForm).applicationPath = $event),
                    disabled: !unref(editStatus)
                  }, null, 8, ["modelValue", "disabled"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "所属场景",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_select, {
                    modelValue: unref(applicationForm).scenario,
                    "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => unref(applicationForm).scenario = $event),
                    placeholder: "请选择",
                    disabled: ""
                  }, null, 8, ["modelValue"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "所属领域",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_select, {
                    modelValue: unref(applicationForm).domain,
                    "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => unref(applicationForm).domain = $event),
                    placeholder: "请选择",
                    disabled: ""
                  }, null, 8, ["modelValue"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, {
                label: "发布情况",
                class: "application-form-item"
              }, {
                default: withCtx(() => [
                  unref(applicationForm).isPublish ? (openBlock(), createBlock(_component_el_tag, {
                    key: 0,
                    type: "success",
                    size: "large"
                  }, {
                    default: withCtx(() => [
                      createTextVNode("已发布")
                    ]),
                    _: 1
                  })) : createCommentVNode("", true),
                  !unref(applicationForm).isPublish ? (openBlock(), createBlock(_component_el_tag, {
                    key: 1,
                    type: "warning",
                    size: "large"
                  }, {
                    default: withCtx(() => [
                      createTextVNode("编辑中")
                    ]),
                    _: 1
                  })) : createCommentVNode("", true)
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, { class: "application-form-item" }, {
                default: withCtx(() => [
                  !unref(editStatus) ? (openBlock(), createBlock(_component_el_button, {
                    key: 0,
                    type: "primary",
                    onClick: _cache[5] || (_cache[5] = ($event) => editStatus.value = true)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("编辑")
                    ]),
                    _: 1
                  })) : createCommentVNode("", true),
                  unref(editStatus) ? (openBlock(), createBlock(_component_el_button, {
                    key: 1,
                    type: "primary",
                    onClick: _cache[6] || (_cache[6] = ($event) => editStatus.value = false)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("保存")
                    ]),
                    _: 1
                  })) : createCommentVNode("", true),
                  unref(editStatus) ? (openBlock(), createBlock(_component_el_button, {
                    key: 2,
                    type: "primary",
                    onClick: _cache[7] || (_cache[7] = ($event) => editStatus.value = false)
                  }, {
                    default: withCtx(() => [
                      createTextVNode("取消")
                    ]),
                    _: 1
                  })) : createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["form"])
        ])
      ], 64);
    };
  }
});/* unplugin-vue-components disabled */const DesignIndex = /* @__PURE__ */ _export_sfc(_sfc_main$c, [["__scopeId", "data-v-134b9776"]]);const _hoisted_1$b = { class: "body-box" };
const _hoisted_2$8 = { class: "dialog-footer" };
const _sfc_main$b = /* @__PURE__ */ defineComponent({
  __name: "DesignProcess",
  setup(__props) {
    const router = useRouter();
    const buttonGroup = [
      {
        code: "newBusinessProcess",
        name: "新增业务流程",
        type: "primary"
      }
    ];
    onActivated(() => {
      processList.value = [
        {
          code: "ConferenceService",
          name: "咖啡服务",
          imageUrl: getAssetsFile("images/process.png")
        }
      ];
    });
    const state = reactive({
      processList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        }
      ],
      applicationId: "",
      applicationName: "",
      dialogVisible: false,
      radio: "head"
    });
    const { processList, dropDownItems, applicationId, applicationName, dialogVisible, radio } = toRefs(state);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handleHeaderButtonClick = (code) => {
      if (code == "newBusinessProcess") {
        dialogVisible.value = true;
      }
    };
    const handleCommand = (process, command) => {
      console.log("Clicked item:", process, command);
    };
    const handleClick = (process) => {
      console.log(process);
      const fullPath = router.resolve(`/design/edit/process?processId=${process.code}&processName=${process.name}`).href;
      window.open(fullPath, "_blank");
    };
    const addProcess = () => {
      dialogVisible.value = false;
      console.log(radio.value);
    };
    return (_ctx, _cache) => {
      const _component_el_radio = ElRadio;
      const _component_el_radio_group = ElRadioGroup;
      const _component_el_button = ElButton;
      const _component_el_dialog = ElDialog;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——业务流程列表",
          "button-group": buttonGroup,
          onButtonClick: handleHeaderButtonClick
        }, null, 8, ["title"]),
        createBaseVNode("div", _hoisted_1$b, [
          (openBlock(true), createElementBlock(Fragment, null, renderList(unref(processList), (process, index) => {
            return openBlock(), createElementBlock("div", { key: index }, [
              createVNode(Card, {
                cardItem: process,
                dropDownItems: unref(dropDownItems),
                onCommandClick: ($event) => handleCommand(process, $event),
                onItemClick: ($event) => handleClick(process)
              }, null, 8, ["cardItem", "dropDownItems", "onCommandClick", "onItemClick"])
            ]);
          }), 128))
        ]),
        createVNode(_component_el_dialog, {
          modelValue: unref(dialogVisible),
          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => isRef(dialogVisible) ? dialogVisible.value = $event : null),
          title: "新增业务流程",
          width: "500"
        }, {
          footer: withCtx(() => [
            createBaseVNode("div", _hoisted_2$8, [
              createVNode(_component_el_button, {
                onClick: _cache[1] || (_cache[1] = ($event) => dialogVisible.value = false)
              }, {
                default: withCtx(() => [
                  createTextVNode("取消")
                ]),
                _: 1
              }),
              createVNode(_component_el_button, {
                type: "primary",
                onClick: addProcess
              }, {
                default: withCtx(() => [
                  createTextVNode(" 确定 ")
                ]),
                _: 1
              })
            ])
          ]),
          default: withCtx(() => [
            createVNode(_component_el_radio_group, {
              modelValue: unref(radio),
              "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => isRef(radio) ? radio.value = $event : null)
            }, {
              default: withCtx(() => [
                createVNode(_component_el_radio, { value: "head" }, {
                  default: withCtx(() => [
                    createTextVNode("从头创建")
                  ]),
                  _: 1
                }),
                createVNode(_component_el_radio, { value: "template" }, {
                  default: withCtx(() => [
                    createTextVNode("从模板创建")
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }, 8, ["modelValue"])
          ]),
          _: 1
        }, 8, ["modelValue"])
      ], 64);
    };
  }
});const _hoisted_1$a = { class: "body-box" };
const _hoisted_2$7 = /* @__PURE__ */ createBaseVNode("div", { class: "title-box" }, "应用页面", -1);
const _hoisted_3$7 = { class: "table-box" };
const _hoisted_4$7 = { class: "table-row" };
const _hoisted_5$7 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "页面名称", -1);
const _hoisted_6$5 = { class: "table-cell" };
const _hoisted_7$4 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "所属页面流", -1);
const _hoisted_8$3 = { class: "table-cell" };
const _hoisted_9$3 = { class: "table" };
const _sfc_main$a = /* @__PURE__ */ defineComponent({
  __name: "DesignPage",
  setup(__props) {
    const router = useRouter();
    const buttonGroup = [
      {
        code: "newPage",
        name: "新增页面",
        type: "primary"
      }
    ];
    onActivated(() => {
      pageList.value = [
        {
          code: "CoffeeServicePage",
          name: "咖啡自助服务页面",
          imageUrl: getAssetsFile("images/page.png")
        }
      ];
    });
    const value = ref("");
    const options = [
      {
        value: "CoffeeFlow",
        label: "咖啡点单页面流"
      },
      {
        value: "MeetingFlow",
        label: "会议室预定页面流"
      }
    ];
    const state = reactive({
      pageList: [],
      dropDownItems: [
        {
          code: "rename",
          name: "重命名"
        },
        {
          code: "delete",
          name: "删除"
        }
      ],
      applicationId: "",
      applicationName: "",
      header: [
        {
          code: "name",
          name: "页面名称",
          type: "String"
        },
        {
          code: "code",
          name: "页面编码",
          type: "String"
        },
        {
          code: "page_flow",
          name: "所在页面流",
          type: "String"
        }
      ],
      data: [
        {
          name: "咖啡点单页面",
          code: "CoffeeMakerPage",
          page_flow: "咖啡点单页面流"
        },
        {
          name: "会议室预定页面",
          code: "ConferenceBookingPage",
          page_flow: "会议室预定页面流"
        }
      ]
    });
    const { pageList, dropDownItems, applicationId, applicationName, header, data } = toRefs(state);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handleClick = (page) => {
      console.log(page);
      const fullPath = router.resolve(`/design/edit/page?pageId=${page.code}&pageName=${page.name}`).href;
      window.open(fullPath, "_blank");
    };
    const handlePageClick = () => {
      console.log("create the page");
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——应用页面列表",
          "button-group": buttonGroup,
          onButtonClick: handlePageClick
        }, null, 8, ["title"]),
        createBaseVNode("div", _hoisted_1$a, [
          _hoisted_2$7,
          createBaseVNode("div", _hoisted_3$7, [
            createBaseVNode("div", _hoisted_4$7, [
              _hoisted_5$7,
              createBaseVNode("div", _hoisted_6$5, [
                createVNode(_component_el_input, {
                  style: { "width": "240px" },
                  placeholder: "请输入"
                })
              ]),
              _hoisted_7$4,
              createBaseVNode("div", _hoisted_8$3, [
                createVNode(_component_el_select, {
                  modelValue: value.value,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => value.value = $event),
                  placeholder: "请选择",
                  style: { "width": "240px" }
                }, {
                  default: withCtx(() => [
                    (openBlock(), createElementBlock(Fragment, null, renderList(options, (item) => {
                      return createVNode(_component_el_option, {
                        key: item.value,
                        label: item.label,
                        value: item.value
                      }, null, 8, ["label", "value"]);
                    }), 64))
                  ]),
                  _: 1
                }, 8, ["modelValue"])
              ]),
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-left": "30px" }
              }, {
                default: withCtx(() => [
                  createTextVNode("筛选")
                ]),
                _: 1
              })
            ]),
            createBaseVNode("div", _hoisted_9$3, [
              createVNode(_sfc_main$v, {
                header: unref(header),
                data: unref(data),
                onHandleEdit: handleClick
              }, null, 8, ["header", "data"])
            ])
          ])
        ])
      ], 64);
    };
  }
});const _hoisted_1$9 = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_2$6 = { class: "sub-title" };
const _hoisted_3$6 = { style: { "display": "flex", "flex-wrap": "wrap", "gap": "2px" } };
const _hoisted_4$6 = { class: "tool-icon" };
const _hoisted_5$6 = { class: "tool-name" };
const _sfc_main$9 = /* @__PURE__ */ defineComponent({
  __name: "index",
  props: {
    designerType: String,
    toolboxJson: Array
  },
  setup(__props) {
    const props = __props;
    const itemList = ref(props.toolboxJson);
    const state = reactive({
      deviceType: {
        type: "",
        name: "",
        items: []
      }
    });
    const { deviceType } = toRefs(state);
    onMounted(() => {
      if (props.designerType === "Process") {
        deviceType.value.type = "domainDevice";
        deviceType.value.name = "领域设备类型控件";
        deviceType.value.items = [];
        getDeviceType();
      }
    });
    const getDeviceType = () => {
      loadDomainBindingData("Device", "SmartBuilding").then((res) => {
        if (res.status === 200) {
          const deviceTypeList = res.data.map((v) => {
            return {
              id: v.deviceTypeCode,
              type: "deviceType",
              name: v.deviceTypeName,
              category: "command",
              icon: "device/" + v.deviceTypeCode + ".png"
            };
          });
          deviceType.value.type = "domainDevice";
          deviceType.value.name = "领域设备类型控件";
          deviceType.value.items = deviceTypeList;
          itemList.value.push(deviceType.value);
          console.log(itemList);
        }
      });
    };
    const requireIcon = (iconPath) => {
      if (iconPath === "") {
        return getAssetsFile("logo.png");
      } else {
        return getAssetsFile(iconPath);
      }
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_image = ElImage;
      return openBlock(), createElementBlock("div", null, [
        (openBlock(true), createElementBlock(Fragment, null, renderList(unref(itemList), (itemCategory) => {
          return openBlock(), createElementBlock("div", {
            key: itemCategory.type
          }, [
            createBaseVNode("div", _hoisted_1$9, [
              createBaseVNode("div", _hoisted_2$6, toDisplayString(itemCategory.name), 1),
              createVNode(_component_el_button, {
                style: { "float": "right" },
                type: "primary",
                plain: ""
              }, {
                default: withCtx(() => [
                  createTextVNode("新增")
                ]),
                _: 1
              })
            ]),
            createBaseVNode("div", _hoisted_3$6, [
              (openBlock(true), createElementBlock(Fragment, null, renderList(itemCategory.items, (item) => {
                return openBlock(), createElementBlock("div", {
                  key: item.id,
                  class: "tool-item"
                }, [
                  createBaseVNode("div", _hoisted_4$6, [
                    createVNode(_component_el_image, {
                      src: requireIcon(item.icon ?? ""),
                      style: { "width": "70%", "margin": "15%" }
                    }, null, 8, ["src"])
                  ]),
                  createBaseVNode("div", _hoisted_5$6, toDisplayString(item.name), 1)
                ]);
              }), 128))
            ])
          ]);
        }), 128))
      ]);
    };
  }
});/* unplugin-vue-components disabled */const ToolBox = /* @__PURE__ */ _export_sfc(_sfc_main$9, [["__scopeId", "data-v-b29d4014"]]);var _interopRequireDefault = (_interopRequireDefault$1 || __CJS__import__0__);
var _aceBuilds = _interopRequireDefault((ace || __CJS__import__1__));
var _themeGithubUrl = _interopRequireDefault((themeGithub ));
var _themeChromeUrl = _interopRequireDefault((themeChrome ));
var _themeMonokaiUrl = _interopRequireDefault((themeMonokai ));
var _modeJsonUrl = _interopRequireDefault((modeJson ));
var _modeJavascriptUrl = _interopRequireDefault((modeJavascript ));
var _modeHtmlUrl = _interopRequireDefault((modeHtml ));
var _modePythonUrl = _interopRequireDefault((modePython ));
var _workerBaseUrl = _interopRequireDefault((workerBase ));
var _workerJsonUrl = _interopRequireDefault((workerJson ));
var _workerJavascriptUrl = _interopRequireDefault((workerJavascript ));
var _workerHtmlUrl = _interopRequireDefault((workerHtml ));
var _jsonUrl = _interopRequireDefault((json ));
var _javascriptUrl = _interopRequireDefault((javascript ));
var _htmlUrl = _interopRequireDefault((html ));
var _pythonUrl = _interopRequireDefault((python ));
var _extSearchboxUrl = _interopRequireDefault((extSearchbox ));
// ace配置，使用动态加载来避免第一次加载开销

// 导入不同的主题模块，并设置对应 URL

_aceBuilds.default.config.setModuleUrl("ace/theme/github", _themeGithubUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/theme/chrome", _themeChromeUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/theme/monokai", _themeMonokaiUrl.default);

// 导入不同语言的语法模式模块，并设置对应 URL (所有支持的主题和模式：node_modules/ace-builds/src-noconflict)

_aceBuilds.default.config.setModuleUrl("ace/mode/json", _modeJsonUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/mode/javascript", _modeJavascriptUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/mode/html", _modeHtmlUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/mode/yaml", _modePythonUrl.default);

// 用于完成语法检查、代码提示、自动补全等代码编辑功能，必须注册模块 ace/mode/lang _ worker，并设置选项 useWorker: true

_aceBuilds.default.config.setModuleUrl("ace/mode/base", _workerBaseUrl.default);
// for vite
_aceBuilds.default.config.setModuleUrl("ace/mode/json_worker", _workerJsonUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/mode/javascript_worker", _workerJavascriptUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/mode/html_worker", _workerHtmlUrl.default);

// 导入不同语言的代码片段，提供代码自动补全和代码块功能

_aceBuilds.default.config.setModuleUrl("ace/snippets/json", _jsonUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/snippets/javascript", _javascriptUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/snippets/html", _htmlUrl.default);
_aceBuilds.default.config.setModuleUrl("ace/snippets/javascript", _pythonUrl.default);

// 启用自动补全等高级编辑支持，

_aceBuilds.default.config.setModuleUrl("ace/ext/searchbox", _extSearchboxUrl.default);

// 启用自动补全等高级编辑支持

_aceBuilds.default.require("ace/ext/language_tools");const saveFileData = (filePath, content) => service({
  url: `/file/save`,
  method: "post",
  data: {
    filePath,
    content
  }
});const processTool = [
	{
		type: "flow-template",
		name: "流程模板",
		items: [
			{
				id: "Reservation",
				type: "process",
				name: "预约流程",
				category: "flow-template",
				icon: "icon/process-template.png"
			}
		]
	},
	{
		type: "flow",
		name: "流程类控件",
		items: [
			{
				id: "StartNode",
				type: "flow-group",
				name: "开始节点",
				category: "flow",
				icon: "icon/startNode.png"
			},
			{
				id: "EndNode",
				type: "icon/flow-group",
				name: "结束节点",
				category: "flow",
				icon: "endNode.png"
			},
			{
				id: "XORNode",
				type: "flow-group",
				name: "异或网关",
				category: "flow",
				icon: "icon/xor.png"
			},
			{
				id: "ParallelNode",
				type: "flow-group",
				name: "并行网关",
				category: "flow",
				icon: "icon/parallel.png"
			},
			{
				id: "DecisionNode",
				type: "flow-group",
				name: "决策节点",
				category: "flow",
				icon: "icon/decision.png"
			},
			{
				id: "LoopNode",
				type: "flow-group",
				name: "循环节点",
				category: "flow",
				icon: "icon/xunhuan.png"
			}
		]
	}
];const _withScopeId$3 = (n) => (pushScopeId("data-v-fca4fc6f"), n = n(), popScopeId(), n);
const _hoisted_1$8 = { style: { "display": "flex", "flex-direction": "column", "height": "100%" } };
const _hoisted_2$5 = { class: "process-header" };
const _hoisted_3$5 = { class: "process-content" };
const _hoisted_4$5 = { style: { "border": "1px solid lightgray", "padding": "20px", "width": "25%", "overflow-y": "auto" } };
const _hoisted_5$5 = /* @__PURE__ */ _withScopeId$3(() => /* @__PURE__ */ createBaseVNode("div", { style: { "margin-bottom": "10px" } }, "工具栏", -1));
const _hoisted_6$4 = { style: { "border": "1px solid lightgray", "padding": "20px", "margin-left": "20px", "width": "50%", "overflow-y": "auto" } };
const _hoisted_7$3 = { style: { "border": "1px solid lightgray", "padding": "20px", "margin-left": "20px", "width": "20%", "overflow-y": "auto" } };
const _sfc_main$8 = /* @__PURE__ */ defineComponent({
  __name: "ProcessEdit",
  setup(__props) {
    const state = reactive({
      processId: "",
      processName: ""
    });
    const boardImage = getAssetsFile("images/board.png");
    const { processId, processName } = toRefs(state);
    const router = useRouter();
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.processId === "string") {
        processId.value = router.currentRoute.value.query.processId || "";
      }
      if (typeof router.currentRoute.value.query.processName === "string") {
        processName.value = router.currentRoute.value.query.processName || "";
      }
    });
    const content = ref("");
    reactive({
      useWorker: true,
      // 启用语法检查,必须为true
      enableBasicAutocompletion: true,
      // 自动补全
      enableLiveAutocompletion: true,
      // 智能补全
      enableSnippets: true,
      // 启用代码段
      showPrintMargin: false,
      // 去掉灰色的线，printMarginColumn
      highlightActiveLine: true,
      // 高亮行
      highlightSelectedWord: true,
      // 高亮选中的字符
      tabSize: 2,
      // tab锁进字符
      fontSize: 14,
      // 设置字号
      wrap: false,
      // 是否换行
      readOnly: false
      // 是否可编辑
    });
    onMounted(async () => {
    });
    const flowPublish = () => {
      ElMessage.success("流程发布成功");
    };
    const saveFile = () => {
      console.log("save");
      saveFileData("SmartBuilding/BuildingA/application/GuestReception/process/ConferenceService.proc", content.value);
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_image = ElImage;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      return openBlock(), createElementBlock("div", _hoisted_1$8, [
        createBaseVNode("div", _hoisted_2$5, [
          createBaseVNode("div", null, toDisplayString(unref(processName)) + "-流程编辑页面", 1),
          createBaseVNode("div", null, [
            createVNode(_component_el_button, {
              type: "primary",
              onClick: saveFile
            }, {
              default: withCtx(() => [
                createTextVNode("保存")
              ]),
              _: 1
            }),
            createVNode(_component_el_button, { type: "warning" }, {
              default: withCtx(() => [
                createTextVNode("代码检查")
              ]),
              _: 1
            }),
            createVNode(_component_el_button, {
              type: "primary",
              onClick: flowPublish
            }, {
              default: withCtx(() => [
                createTextVNode("流程发布")
              ]),
              _: 1
            })
          ])
        ]),
        createBaseVNode("div", _hoisted_3$5, [
          createBaseVNode("div", _hoisted_4$5, [
            _hoisted_5$5,
            createVNode(_component_el_input, {
              placeholder: "搜索",
              "prefix-icon": unref(search_default),
              style: { "margin-bottom": "10px" }
            }, null, 8, ["prefix-icon"]),
            createVNode(ToolBox, {
              toolboxJson: unref(processTool),
              designerType: "Process"
            }, null, 8, ["toolboxJson"])
          ]),
          createBaseVNode("div", _hoisted_6$4, [
            createVNode(_component_el_image, { src: unref(boardImage) }, null, 8, ["src"])
          ]),
          createBaseVNode("div", _hoisted_7$3, [
            createTextVNode(" 属性配置区 "),
            createVNode(_component_el_form, null, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, {
                  label: "id",
                  class: "attribute-item"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, { placeholder: "请输入" })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ])
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled */const ProcessEdit = /* @__PURE__ */ _export_sfc(_sfc_main$8, [["__scopeId", "data-v-fca4fc6f"]]);const pageTool = [
	{
		type: "domainUI",
		name: "领域UI控件",
		items: [
			{
				id: "ScenarioNavigation",
				type: "navigation",
				name: "场景化导航",
				category: "container",
				icon: "guide.svg"
			},
			{
				id: "BarChart",
				type: "chart-group",
				name: "柱状图",
				category: "chart",
				icon: "bar.svg"
			}
		]
	},
	{
		type: "input",
		name: "输入类控件",
		items: [
			{
				id: "TextBox",
				type: "input-group",
				name: "文本",
				category: "input",
				icon: "text.png"
			},
			{
				id: "Image",
				type: "image",
				name: "图像",
				category: "input",
				icon: "image.png"
			}
		]
	},
	{
		type: "command",
		name: "命令类控件",
		items: [
			{
				id: "Button",
				type: "button-group",
				name: "按钮",
				category: "command",
				icon: "button.png"
			}
		]
	},
	{
		type: "container",
		name: "容器类控件",
		items: [
			{
				id: "ContentContainer",
				type: "content-container",
				name: "容器",
				category: "container",
				icon: "container.png"
			},
			{
				id: "ModalContainer",
				type: "modal-container",
				name: "弹窗容器",
				category: "container",
				icon: "modal.png"
			},
			{
				id: "ScenarioNavigation",
				type: "nav",
				name: "场景化导航",
				category: "container",
				icon: "guide.svg"
			}
		]
	}
];const _withScopeId$2 = (n) => (pushScopeId("data-v-875009c3"), n = n(), popScopeId(), n);
const _hoisted_1$7 = { style: { "display": "flex", "flex-direction": "column", "height": "100%" } };
const _hoisted_2$4 = { class: "page-header" };
const _hoisted_3$4 = { class: "page-container" };
const _hoisted_4$4 = { style: { "border": "1px solid lightgray", "padding": "20px", "width": "25%", "overflow-y": "auto" } };
const _hoisted_5$4 = /* @__PURE__ */ _withScopeId$2(() => /* @__PURE__ */ createBaseVNode("div", { style: { "margin-bottom": "10px" } }, "工具栏", -1));
const _hoisted_6$3 = { style: { "border": "1px solid lightgray", "padding": "20px", "margin-left": "20px", "width": "50%", "overflow-y": "auto" } };
const _hoisted_7$2 = { style: { "margin-top": "20px" } };
const _hoisted_8$2 = { style: { "width": "50%" } };
const _hoisted_9$2 = { style: { "border": "1px solid lightgray", "padding": "20px", "margin-left": "20px", "width": "20%", "overflow-y": "auto" } };
const _hoisted_10 = { style: { "font-size": "15px", "color": "gray", "margin-bottom": "10px" } };
const _hoisted_11 = { style: { "width": "50%" } };
const _sfc_main$7 = /* @__PURE__ */ defineComponent({
  __name: "PageEdit",
  setup(__props) {
    const router = useRouter();
    const doPublish = () => {
      ElMessage.success("应用发布成功");
    };
    const goToPage = () => {
      const fullPath = router.resolve(`/demo/page?processId=${buttonConfig.value.processId}`).href;
      window.open(fullPath, "_blank");
    };
    const formRefs = ref({});
    const state = reactive({
      pageId: "",
      pageName: "",
      config: {},
      rules: {},
      commonConfig: {},
      buttonConfig: {},
      selectItem: ""
    });
    const { pageId, pageName, config, rules, buttonConfig, selectItem, commonConfig } = toRefs(state);
    onMounted(() => {
      fetchConfig();
    });
    const fetchConfig = () => {
      config.value = [{
        code: "coffeeType",
        name: "咖啡类型",
        type: "Enum",
        optional: ["摩卡", "美式"],
        value: ""
      }];
      rules.value = {};
      config.value.forEach((configItem) => {
        rules.value[configItem.code] = [
          { required: true, message: `${configItem.name} is required`, trigger: "blur" }
        ];
      });
      commonConfig.value = {
        id: ""
      };
      buttonConfig.value = {
        processId: "",
        params: []
      };
    };
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.pageId === "string") {
        pageId.value = router.currentRoute.value.query.pageId || "";
      }
      if (typeof router.currentRoute.value.query.pageName === "string") {
        pageName.value = router.currentRoute.value.query.pageName || "";
      }
    });
    watchEffect(() => {
      commonConfig.value.id = selectItem.value;
    });
    const handleProcessChange = (processId) => {
      if (processId === "ConferenceService") {
        buttonConfig.value.params = [
          {
            "actionId": "makeCoffee",
            "actionName": "制作咖啡",
            "executeArgs": [
              {
                code: "coffeeType",
                name: "咖啡类型",
                value: "${coffeeType}"
              }
            ]
          }
        ];
      } else {
        buttonConfig.value.params = [];
      }
    };
    return (_ctx, _cache) => {
      const _component_el_button = ElButton;
      const _component_el_input = ElInput;
      const _component_el_breadcrumb_item = ElBreadcrumbItem;
      const _component_el_breadcrumb = ElBreadcrumb;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_form_item = ElFormItem;
      const _component_el_form = ElForm;
      return openBlock(), createElementBlock("div", _hoisted_1$7, [
        createBaseVNode("div", _hoisted_2$4, [
          createBaseVNode("div", null, toDisplayString(unref(pageName)) + "-页面编辑", 1),
          createBaseVNode("div", null, [
            createVNode(_component_el_button, { type: "primary" }, {
              default: withCtx(() => [
                createTextVNode("保存")
              ]),
              _: 1
            }),
            createVNode(_component_el_button, {
              type: "primary",
              onClick: goToPage
            }, {
              default: withCtx(() => [
                createTextVNode("试运行")
              ]),
              _: 1
            }),
            createVNode(_component_el_button, {
              type: "primary",
              onClick: doPublish,
              plain: ""
            }, {
              default: withCtx(() => [
                createTextVNode("应用发布")
              ]),
              _: 1
            })
          ])
        ]),
        createBaseVNode("div", _hoisted_3$4, [
          createBaseVNode("div", _hoisted_4$4, [
            _hoisted_5$4,
            createVNode(_component_el_input, {
              placeholder: "搜索",
              "prefix-icon": unref(search_default)
            }, null, 8, ["prefix-icon"]),
            createVNode(ToolBox, {
              toolboxJson: unref(pageTool),
              designerType: "Page"
            }, null, 8, ["toolboxJson"])
          ]),
          createBaseVNode("div", _hoisted_6$3, [
            createTextVNode(" 编辑区 "),
            createBaseVNode("div", _hoisted_7$2, [
              createVNode(_component_el_breadcrumb, { "separator-icon": unref(arrow_right_default) }, {
                default: withCtx(() => [
                  createVNode(_component_el_breadcrumb_item, null, {
                    default: withCtx(() => [
                      createTextVNode("咖啡自助服务")
                    ]),
                    _: 1
                  }),
                  createVNode(_component_el_breadcrumb_item, null, {
                    default: withCtx(() => [
                      createTextVNode("点咖啡")
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["separator-icon"])
            ]),
            createVNode(_component_el_form, {
              model: unref(config),
              rules: unref(rules),
              ref_key: "formRefs",
              ref: formRefs,
              style: { "margin-top": "10px" }
            }, {
              default: withCtx(() => [
                (openBlock(true), createElementBlock(Fragment, null, renderList(unref(config), (configItem) => {
                  return openBlock(), createBlock(_component_el_form_item, {
                    label: configItem.name,
                    key: configItem.code,
                    required: "",
                    prop: configItem.code
                  }, {
                    default: withCtx(() => [
                      createBaseVNode("div", _hoisted_8$2, [
                        configItem.type === "String" ? (openBlock(), createBlock(_component_el_input, {
                          key: 0,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event,
                          id: configItem.code,
                          onClick: ($event) => selectItem.value = unref(selectItem) !== configItem.code ? selectItem.value = configItem.code : selectItem.value = ""
                        }, null, 8, ["modelValue", "onUpdate:modelValue", "id", "onClick"])) : createCommentVNode("", true),
                        configItem.type === "Number" ? (openBlock(), createBlock(_component_el_input, {
                          key: 1,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event,
                          type: "number"
                        }, null, 8, ["modelValue", "onUpdate:modelValue"])) : createCommentVNode("", true),
                        configItem.type === "Enum" ? (openBlock(), createBlock(_component_el_select, {
                          key: 2,
                          modelValue: configItem.value,
                          "onUpdate:modelValue": ($event) => configItem.value = $event,
                          id: configItem.code,
                          onClick: ($event) => selectItem.value = unref(selectItem) !== configItem.code ? selectItem.value = configItem.code : selectItem.value = ""
                        }, {
                          default: withCtx(() => [
                            (openBlock(true), createElementBlock(Fragment, null, renderList(configItem.optional, (option) => {
                              return openBlock(), createBlock(_component_el_option, {
                                key: option,
                                label: option,
                                value: option
                              }, null, 8, ["label", "value"]);
                            }), 128))
                          ]),
                          _: 2
                        }, 1032, ["modelValue", "onUpdate:modelValue", "id", "onClick"])) : createCommentVNode("", true)
                      ])
                    ]),
                    _: 2
                  }, 1032, ["label", "prop"]);
                }), 128))
              ]),
              _: 1
            }, 8, ["model", "rules"]),
            createBaseVNode("div", null, [
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-top": "10px" },
                onClick: _cache[0] || (_cache[0] = ($event) => unref(selectItem) !== "button-52152" ? selectItem.value = "button-52152" : selectItem.value = ""),
                id: "button-52152"
              }, {
                default: withCtx(() => [
                  createTextVNode(" 下单 ")
                ]),
                _: 1
              })
            ])
          ]),
          createBaseVNode("div", _hoisted_9$2, [
            createTextVNode(" 属性配置区 "),
            unref(selectItem) !== "" ? (openBlock(), createBlock(_component_el_form, {
              key: 0,
              model: unref(commonConfig),
              ref: "configFormRefs",
              style: { "margin-top": "10px" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, { label: "id" }, {
                  default: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: unref(commonConfig).id,
                      "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(commonConfig).id = $event),
                      disabled: ""
                    }, null, 8, ["modelValue"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }, 8, ["model"])) : createCommentVNode("", true),
            unref(selectItem) === "button-52152" ? (openBlock(), createBlock(_component_el_form, {
              key: 1,
              model: unref(buttonConfig),
              ref: "configFormRefs",
              style: { "margin-top": "10px" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form_item, { label: "功能" }, {
                  default: withCtx(() => [
                    createVNode(_component_el_select, {
                      modelValue: unref(buttonConfig).processId,
                      "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => unref(buttonConfig).processId = $event),
                      onChange: handleProcessChange,
                      clearable: ""
                    }, {
                      default: withCtx(() => [
                        (openBlock(), createElementBlock(Fragment, null, renderList([{ label: "咖啡服务", value: "ConferenceService" }], (option) => {
                          return createVNode(_component_el_option, {
                            key: option.value,
                            label: option.label,
                            value: option.value
                          }, null, 8, ["label", "value"]);
                        }), 64))
                      ]),
                      _: 1
                    }, 8, ["modelValue"])
                  ]),
                  _: 1
                }),
                (openBlock(true), createElementBlock(Fragment, null, renderList(unref(buttonConfig).params, (action) => {
                  return openBlock(), createElementBlock("div", null, [
                    createBaseVNode("div", _hoisted_10, toDisplayString(action.actionName), 1),
                    (openBlock(true), createElementBlock(Fragment, null, renderList(action.executeArgs, (configItem) => {
                      return openBlock(), createBlock(_component_el_form_item, {
                        label: configItem.name,
                        key: configItem.code,
                        prop: configItem.code,
                        style: { "margin-left": "30px" }
                      }, {
                        default: withCtx(() => [
                          createBaseVNode("div", _hoisted_11, [
                            createVNode(_component_el_input, {
                              modelValue: configItem.value,
                              "onUpdate:modelValue": ($event) => configItem.value = $event
                            }, null, 8, ["modelValue", "onUpdate:modelValue"])
                          ])
                        ]),
                        _: 2
                      }, 1032, ["label", "prop"]);
                    }), 128))
                  ]);
                }), 256))
              ]),
              _: 1
            }, 8, ["model"])) : createCommentVNode("", true)
          ])
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled */const PageEdit = /* @__PURE__ */ _export_sfc(_sfc_main$7, [["__scopeId", "data-v-875009c3"]]);const getDeveloperConfig = () => service({
  url: `/setting/developer`,
  method: "get"
});
const editDeveloperConfig = (config) => service({
  url: `/setting/developer/edit`,
  method: "post",
  data: config
});const _hoisted_1$6 = /* @__PURE__ */ createBaseVNode("div", null, "开发者设置", -1);
const _sfc_main$6 = /* @__PURE__ */ defineComponent({
  __name: "index",
  setup(__props) {
    const state = reactive({
      form: {
        mavenPath: "",
        workspacePath: "",
        projectPath: "",
        definitionPath: ""
      },
      editable: false,
      rawData: {
        mavenPath: "",
        workspacePath: "",
        projectPath: "",
        definitionPath: ""
      }
    });
    const { form, editable, rawData } = toRefs(state);
    onMounted(() => {
      getConfig();
    });
    const getConfig = () => {
      getDeveloperConfig().then((res) => {
        if (res.status == 200) {
          form.value = { ...res.data };
          rawData.value = { ...res.data };
        }
      });
    };
    const cancelEdit = () => {
      form.value = { ...rawData.value };
      editable.value = false;
    };
    const saveEdit = () => {
      editDeveloperConfig(form.value).then((res) => {
        if (res.status == 200) {
          ElMessage.success(res.data);
          rawData.value = { ...form.value };
          editable.value = false;
        }
      });
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_form_item = ElFormItem;
      const _component_el_button = ElButton;
      const _component_el_form = ElForm;
      return openBlock(), createElementBlock(Fragment, null, [
        _hoisted_1$6,
        createVNode(_component_el_form, {
          model: unref(form),
          "label-width": "auto",
          style: { "max-width": "50%", "margin": "20px" }
        }, {
          default: withCtx(() => [
            createVNode(_component_el_form_item, { label: "maven目录" }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: unref(form).mavenPath,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => unref(form).mavenPath = $event),
                  disabled: !unref(editable)
                }, null, 8, ["modelValue", "disabled"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, { label: "工作区目录" }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: unref(form).workspacePath,
                  "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => unref(form).workspacePath = $event),
                  disabled: !unref(editable),
                  onChange: _cache[2] || (_cache[2] = (v) => {
                    unref(form).definitionPath = v + "deviceType/";
                  })
                }, null, 8, ["modelValue", "disabled"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, { label: "设备打包临时目录" }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: unref(form).projectPath,
                  "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => unref(form).projectPath = $event),
                  disabled: !unref(editable)
                }, null, 8, ["modelValue", "disabled"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, { label: "设备定义工作目录" }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: unref(form).definitionPath,
                  "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => unref(form).definitionPath = $event),
                  disabled: ""
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, null, {
              default: withCtx(() => [
                !unref(editable) ? (openBlock(), createBlock(_component_el_button, {
                  key: 0,
                  type: "primary",
                  onClick: _cache[5] || (_cache[5] = ($event) => editable.value = true)
                }, {
                  default: withCtx(() => [
                    createTextVNode("修改")
                  ]),
                  _: 1
                })) : createCommentVNode("", true),
                unref(editable) ? (openBlock(), createBlock(_component_el_button, {
                  key: 1,
                  onClick: cancelEdit
                }, {
                  default: withCtx(() => [
                    createTextVNode("取消")
                  ]),
                  _: 1
                })) : createCommentVNode("", true),
                unref(editable) ? (openBlock(), createBlock(_component_el_button, {
                  key: 2,
                  type: "primary",
                  onClick: saveEdit
                }, {
                  default: withCtx(() => [
                    createTextVNode("保存")
                  ]),
                  _: 1
                })) : createCommentVNode("", true)
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["model"])
      ], 64);
    };
  }
});const _withScopeId$1 = (n) => (pushScopeId("data-v-e2ab9e91"), n = n(), popScopeId(), n);
const _hoisted_1$5 = { class: "header-panel" };
const _hoisted_2$3 = { class: "header-center" };
const _hoisted_3$3 = /* @__PURE__ */ _withScopeId$1(() => /* @__PURE__ */ createBaseVNode("div", { class: "header-title" }, "在模板库中寻找符合场景的模板", -1));
const _hoisted_4$3 = { class: "header-search" };
const _hoisted_5$3 = { style: { "display": "flex", "justify-content": "space-between" } };
const _hoisted_6$2 = /* @__PURE__ */ _withScopeId$1(() => /* @__PURE__ */ createBaseVNode("div", { style: { "font-weight": "bold", "font-size": "18px" } }, "所有模板", -1));
const _sfc_main$5 = /* @__PURE__ */ defineComponent({
  __name: "TemplateView",
  setup(__props) {
    return (_ctx, _cache) => {
      const _component_el_icon = ElIcon;
      const _component_el_input = ElInput;
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock("div", null, [
        createBaseVNode("div", _hoisted_1$5, [
          createBaseVNode("div", _hoisted_2$3, [
            _hoisted_3$3,
            createBaseVNode("div", _hoisted_4$3, [
              createVNode(_component_el_input, {
                placeholder: "在模板库中搜索",
                class: "search-input"
              }, {
                prefix: withCtx(() => [
                  createVNode(_component_el_icon, {
                    class: "el-input__icon",
                    style: { "font-size": "20px", "color": "#606266" }
                  }, {
                    default: withCtx(() => [
                      createVNode(unref(search_default))
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ])
          ])
        ]),
        createBaseVNode("div", _hoisted_5$3, [
          _hoisted_6$2,
          createVNode(_component_el_button, { type: "primary" }, {
            default: withCtx(() => [
              createTextVNode("添加模板")
            ]),
            _: 1
          })
        ])
      ]);
    };
  }
});/* unplugin-vue-components disabled */const TemplateView = /* @__PURE__ */ _export_sfc(_sfc_main$5, [["__scopeId", "data-v-e2ab9e91"]]);const _withScopeId = (n) => (pushScopeId("data-v-2cbf7032"), n = n(), popScopeId(), n);
const _hoisted_1$4 = { class: "body-box" };
const _hoisted_2$2 = { class: "tool-button" };
const _hoisted_3$2 = /* @__PURE__ */ _withScopeId(() => /* @__PURE__ */ createBaseVNode("span", null, "静态分析工具", -1));
const _hoisted_4$2 = { class: "tool-button" };
const _hoisted_5$2 = /* @__PURE__ */ _withScopeId(() => /* @__PURE__ */ createBaseVNode("span", null, "缺陷检测工具", -1));
const _sfc_main$4 = /* @__PURE__ */ defineComponent({
  __name: "DesignQualityAssurance",
  setup(__props) {
    const StaticAnalysisClick = () => {
      console.log("do the StaticAnalysis");
    };
    const BugAnalysisClick = () => {
      console.log("do the BugAnalysisClick");
    };
    return (_ctx, _cache) => {
      const _component_el_image = ElImage;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, { title: "质量保障工具" }),
        createBaseVNode("div", _hoisted_1$4, [
          createBaseVNode("div", _hoisted_2$2, [
            createVNode(_component_el_image, {
              src: "./src/assets/icon/analysis.png",
              alt: "静态分析",
              onClick: StaticAnalysisClick
            }),
            _hoisted_3$2
          ]),
          createBaseVNode("div", _hoisted_4$2, [
            createVNode(_component_el_image, {
              src: "./src/assets/icon/bug.png",
              alt: "缺陷检测",
              onClick: BugAnalysisClick
            }),
            _hoisted_5$2
          ])
        ])
      ], 64);
    };
  }
});/* unplugin-vue-components disabled */const DesignQualityAssurance = /* @__PURE__ */ _export_sfc(_sfc_main$4, [["__scopeId", "data-v-2cbf7032"]]);const _hoisted_1$3 = { class: "body-box" };
const _hoisted_2$1 = /* @__PURE__ */ createBaseVNode("div", { class: "title-box" }, "场景设备", -1);
const _hoisted_3$1 = { class: "table-box" };
const _hoisted_4$1 = { class: "table-row" };
const _hoisted_5$1 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "设备名称", -1);
const _hoisted_6$1 = { class: "table-cell" };
const _hoisted_7$1 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "设备状态", -1);
const _hoisted_8$1 = { class: "table-cell" };
const _hoisted_9$1 = { class: "table" };
const _sfc_main$3 = /* @__PURE__ */ defineComponent({
  __name: "DesignDeviceLogic",
  setup(__props) {
    const value = ref("");
    const router = useRouter();
    const state = reactive({
      applicationId: "",
      applicationName: "",
      header_public: [
        {
          code: "name",
          name: "设备名称",
          type: "String"
        },
        {
          code: "description",
          name: "位置信息",
          type: "String"
        },
        {
          code: "status",
          name: "逻辑状态",
          type: "String"
        }
      ],
      data_public: [
        {
          name: "咖啡机1",
          description: "物理楼A2003...",
          status: "已启用"
        },
        {
          name: "咖啡机2",
          description: "物理楼1楼水吧",
          status: "已启用"
        }
      ]
    });
    const { applicationId, applicationName, header_public, data_public } = toRefs(state);
    const options = [
      {
        value: "1",
        label: "已启用"
      },
      {
        value: "0",
        label: "未启用"
      }
    ];
    const buttonGroup = [
      {
        code: "newDevice",
        name: "+新增设备",
        type: "primary"
      }
    ];
    const tableButtonGroup = [
      {
        code: "unavailable",
        name: "禁用",
        type: "warning",
        size: "small"
      }
    ];
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handleHeaderButtonClick = () => {
      console.log("create the device logic");
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——设备列表",
          "button-group": buttonGroup,
          onButtonClick: handleHeaderButtonClick
        }, null, 8, ["title"]),
        createBaseVNode("div", _hoisted_1$3, [
          _hoisted_2$1,
          createBaseVNode("div", _hoisted_3$1, [
            createBaseVNode("div", _hoisted_4$1, [
              _hoisted_5$1,
              createBaseVNode("div", _hoisted_6$1, [
                createVNode(_component_el_input, {
                  style: { "width": "240px" },
                  placeholder: "制作"
                })
              ]),
              _hoisted_7$1,
              createBaseVNode("div", _hoisted_8$1, [
                createVNode(_component_el_select, {
                  modelValue: value.value,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => value.value = $event),
                  placeholder: "请选择",
                  style: { "width": "240px" }
                }, {
                  default: withCtx(() => [
                    (openBlock(), createElementBlock(Fragment, null, renderList(options, (item) => {
                      return createVNode(_component_el_option, {
                        key: item.value,
                        label: item.label,
                        value: item.value
                      }, null, 8, ["label", "value"]);
                    }), 64))
                  ]),
                  _: 1
                }, 8, ["modelValue"])
              ]),
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-left": "30px" }
              }, {
                default: withCtx(() => [
                  createTextVNode("筛选")
                ]),
                _: 1
              })
            ]),
            createBaseVNode("div", _hoisted_9$1, [
              createVNode(_sfc_main$v, {
                header: unref(header_public),
                "button-group": tableButtonGroup,
                data: unref(data_public)
              }, null, 8, ["header", "data"])
            ])
          ])
        ])
      ], 64);
    };
  }
});const _hoisted_1$2 = /* @__PURE__ */ createBaseVNode("div", { class: "body-box" }, null, -1);
const _sfc_main$2 = /* @__PURE__ */ defineComponent({
  __name: "DesignMenu",
  setup(__props) {
    const router = useRouter();
    const buttonGroup = [
      {
        code: "newMenu",
        name: "新增菜单",
        type: "primary"
      }
    ];
    const state = reactive({
      applicationId: "",
      applicationName: ""
    });
    const { applicationId, applicationName } = toRefs(state);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handleMenuClick = () => {
      console.log("create the Menu");
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——菜单管理",
          "button-group": buttonGroup,
          onButtonClick: handleMenuClick
        }, null, 8, ["title"]),
        _hoisted_1$2
      ], 64);
    };
  }
});const _hoisted_1$1 = { class: "body-box" };
const _hoisted_2 = /* @__PURE__ */ createBaseVNode("div", { class: "title-box" }, "页面流", -1);
const _hoisted_3 = { class: "table-box" };
const _hoisted_4 = { class: "table-row" };
const _hoisted_5 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "页面流名称", -1);
const _hoisted_6 = { class: "table-cell" };
const _hoisted_7 = /* @__PURE__ */ createBaseVNode("div", { class: "title-cell label" }, "页面流状态", -1);
const _hoisted_8 = { class: "table-cell" };
const _hoisted_9 = { class: "table" };
const _sfc_main$1 = /* @__PURE__ */ defineComponent({
  __name: "DesignPageFlow",
  setup(__props) {
    const router = useRouter();
    const buttonGroup = [
      {
        code: "newPageFlow",
        name: "新增页面流",
        type: "primary"
      }
    ];
    const value = ref("");
    const options = [
      {
        value: "0",
        label: "待发布"
      },
      {
        value: "1",
        label: "已发布"
      }
    ];
    const tableButtonGroup = [
      {
        code: "setting",
        name: "配置",
        type: "info",
        size: "small"
      },
      {
        code: "publish",
        name: "发布",
        type: "success",
        size: "small"
      }
    ];
    const state = reactive({
      applicationId: "",
      applicationName: "",
      header: [
        {
          code: "name",
          name: "页面流名称",
          type: "String"
        },
        {
          code: "code",
          name: "页面流编码",
          type: "String"
        },
        {
          code: "status",
          name: "状态",
          type: "String"
        }
      ],
      data: [
        {
          name: "咖啡点单页面流",
          code: "CoffeeMakerPageflow",
          status: "待发布"
        },
        {
          name: "会议室预定页面流",
          code: "ConferenceBookingPageflow",
          status: "待发布"
        }
      ]
    });
    const { applicationId, applicationName, header, data } = toRefs(state);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handlePageFlowClick = () => {
      console.log("create the pageFlow");
    };
    return (_ctx, _cache) => {
      const _component_el_input = ElInput;
      const _component_el_option = ElOption;
      const _component_el_select = ElSelect;
      const _component_el_button = ElButton;
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——页面流列表",
          "button-group": buttonGroup,
          onButtonClick: handlePageFlowClick
        }, null, 8, ["title"]),
        createBaseVNode("div", _hoisted_1$1, [
          _hoisted_2,
          createBaseVNode("div", _hoisted_3, [
            createBaseVNode("div", _hoisted_4, [
              _hoisted_5,
              createBaseVNode("div", _hoisted_6, [
                createVNode(_component_el_input, {
                  style: { "width": "240px" },
                  placeholder: "请输入"
                })
              ]),
              _hoisted_7,
              createBaseVNode("div", _hoisted_8, [
                createVNode(_component_el_select, {
                  modelValue: value.value,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => value.value = $event),
                  placeholder: "请选择",
                  style: { "width": "240px" }
                }, {
                  default: withCtx(() => [
                    (openBlock(), createElementBlock(Fragment, null, renderList(options, (item) => {
                      return createVNode(_component_el_option, {
                        key: item.value,
                        label: item.label,
                        value: item.value
                      }, null, 8, ["label", "value"]);
                    }), 64))
                  ]),
                  _: 1
                }, 8, ["modelValue"])
              ]),
              createVNode(_component_el_button, {
                type: "primary",
                style: { "margin-left": "30px" }
              }, {
                default: withCtx(() => [
                  createTextVNode("筛选")
                ]),
                _: 1
              })
            ]),
            createBaseVNode("div", _hoisted_9, [
              createVNode(_sfc_main$v, {
                header: unref(header),
                "button-group": tableButtonGroup,
                data: unref(data),
                canEdit: false
              }, null, 8, ["header", "data"])
            ])
          ])
        ])
      ], 64);
    };
  }
});const _hoisted_1 = /* @__PURE__ */ createBaseVNode("div", { class: "body-box" }, null, -1);
const _sfc_main = /* @__PURE__ */ defineComponent({
  __name: "DesignGlobalVariable",
  setup(__props) {
    const router = useRouter();
    const buttonGroup = [
      {
        code: "newGlobalVariable",
        name: "新增全局变量",
        type: "primary"
      }
    ];
    const state = reactive({
      applicationId: "",
      applicationName: ""
    });
    const { applicationId, applicationName } = toRefs(state);
    watchEffect(() => {
      if (typeof router.currentRoute.value.query.applicationId === "string") {
        applicationId.value = router.currentRoute.value.query.applicationId || "";
      }
      if (typeof router.currentRoute.value.query.applicationName === "string") {
        applicationName.value = router.currentRoute.value.query.applicationName || "";
      }
    });
    const handleGlobalVariableClick = () => {
      console.log("create the GlobalVariable");
    };
    return (_ctx, _cache) => {
      return openBlock(), createElementBlock(Fragment, null, [
        createVNode(PageHeader, {
          title: unref(applicationName) + "——全局变量列表",
          "button-group": buttonGroup,
          onButtonClick: handleGlobalVariableClick
        }, null, 8, ["title"]),
        _hoisted_1
      ], 64);
    };
  }
});const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: "",
      name: "首页",
      component: MainLayout,
      meta: {
        title: "首页",
        keepAlive: true
      },
      children: [
        {
          path: "",
          name: "main",
          component: HomeView
        },
        {
          path: "admin",
          name: "系统配置员页面",
          component: null,
          meta: {
            requiresAuth: true,
            roles: ["admin"]
          },
          children: [
            {
              path: "auth",
              name: "权限配置",
              component: null,
              children: []
              // 添加空的 children 属性
            },
            {
              path: "publish-setting",
              name: "应用发布",
              component: null,
              children: []
              // 添加空的 children 属性
            },
            {
              path: "userManage",
              name: "用户管理",
              component: null,
              children: []
              // 添加空的 children 属性
            }
          ]
        },
        {
          path: "business",
          name: "业务人员界面",
          component: null,
          meta: {
            requiresAuth: true,
            roles: ["business"]
          },
          children: [
            {
              path: "businessManage",
              name: "业务管理",
              component: null
            },
            {
              path: "workspace",
              name: "我的空间",
              component: null
            }
          ]
        },
        {
          path: "developer",
          name: "场景配置人员页面",
          component: null,
          meta: {
            requiresAuth: true,
            roles: ["admin", "developer", "business"]
          },
          children: [
            {
              path: "template",
              name: "模板库",
              component: TemplateView
            },
            {
              path: "workspace",
              name: "我的空间",
              component: WorkspaceView
            },
            {
              path: "device-component/detail",
              name: "设备模板详情",
              component: _sfc_main$r
            },
            {
              path: "device-component/add",
              name: "新增设备模板",
              component: _sfc_main$s
            },
            {
              path: "scenario/detail",
              name: "场景详情",
              component: ScenarioDetail
            },
            {
              path: "domain/detail",
              name: "领域详情",
              component: DomainDetail
            },
            {
              path: "setting",
              name: "开发者设置",
              component: _sfc_main$6
            }
          ]
        },
        {
          path: "user",
          name: "普通用户页面",
          component: null,
          meta: {
            requiresAuth: true,
            roles: ["user"]
          },
          children: [
            {
              path: "recommendation",
              name: "为你推荐",
              component: _sfc_main$x
            },
            {
              path: "my-application",
              name: "我的应用",
              component: null
            },
            {
              path: "my-profile",
              name: "个人资料",
              component: null
            },
            {
              path: "my-setting",
              name: "账号设置",
              component: null
            }
          ]
        }
      ]
    },
    {
      path: "/design",
      name: "设计页面",
      component: null,
      children: [
        {
          path: "",
          name: "设计目录",
          component: _sfc_main$e,
          children: [
            {
              path: "index",
              name: "设计首页",
              component: DesignIndex
            },
            {
              path: "process",
              name: "流程列表",
              component: _sfc_main$b
            },
            {
              path: "page",
              name: "页面列表",
              component: _sfc_main$a
            },
            {
              path: "quality_assurance",
              name: "质量保障",
              component: DesignQualityAssurance
            },
            {
              path: "device_logic",
              name: "设备逻辑",
              component: _sfc_main$3
            },
            {
              path: "menu",
              name: "菜单管理",
              component: _sfc_main$2
            },
            {
              path: "page_flow",
              name: "页面流管理",
              component: _sfc_main$1
            },
            {
              path: "global_variable",
              name: "全局变量管理",
              component: _sfc_main
            }
          ]
        },
        {
          path: "edit",
          name: "编辑界面",
          component: _sfc_main$e,
          children: [
            {
              path: "process",
              name: "流程编辑页面",
              component: ProcessEdit
            },
            {
              path: "page",
              name: "视图编辑页面",
              component: PageEdit
            }
          ]
        }
      ]
    },
    {
      path: "/demo",
      name: "应用demo",
      component: null,
      children: [
        {
          path: "",
          name: "智慧楼宇",
          children: [
            {
              path: "",
              name: "场景列表",
              component: _sfc_main$I
            },
            {
              path: "resource",
              name: "资源列表",
              component: _sfc_main$J
            },
            {
              path: "applicationList",
              name: "应用列表",
              component: _sfc_main$K
            },
            {
              path: "application/detail",
              name: "应用功能列表",
              component: _sfc_main$L
            },
            {
              path: "process",
              name: "应用功能流程",
              component: _sfc_main$M
            },
            {
              path: "page",
              name: "应用功能页面",
              component: _sfc_main$N
            },
            {
              path: "mock-device",
              name: "模拟设备页面",
              component: _sfc_main$O
            }
          ]
        }
      ]
    }
  ]
});
router.beforeEach((to, _from, next) => {
  const userStore = useUserStore();
  const { isAuthenticated, roles } = userStore;
  const metaRoles = to.meta.roles;
  if (to.meta.requiresAuth) {
    if (!isAuthenticated) {
      ElMessage.warning("请先登录");
      next("/");
    } else if (to.meta.roles && !metaRoles.some((role) => roles.includes(role))) {
      ElMessage.warning("权限不足");
      next("/");
    } else {
      next();
    }
  } else {
    next();
  }
});const app = createApp(_sfc_main$P);
const pinia = createPinia();
pinia.use(src_default);
app.use(pinia);
app.use(router);
app.use(installer);
app.mount("#app");