<template>
  <div id="viewQuestionView">
    <a-row :gutter="[24, 24]">
      <a-col :md="12" :xs="24">
        <a-tabs default-active-key="question">
          <a-tab-pane key="question" title="题目">
            <a-card v-if="question" :title="question.title">
              <a-descriptions
                title="判题条件"
                :column="{ xs: 1, md: 2, lg: 3 }"
              >
                <a-descriptions-item label="时间限制">
                  {{ question.judgeConfig.timeLimit ?? 0 }} ms
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }} KB
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }} KB
                </a-descriptions-item>
              </a-descriptions>
              <MdViewer :value="question.content || ''" />
              <template #extra>
                <a-space wrap>
                  <a-tag
                    v-for="(tag, index) of question.tags"
                    :key="index"
                    color="green"
                    >{{ tag }}
                  </a-tag>
                </a-space>
              </template>
            </a-card>
          </a-tab-pane>
          <a-tab-pane key="comment" title="评论" disabled> 评论区</a-tab-pane>
          <a-tab-pane key="answer" title="答案"> 暂时无法查看答案</a-tab-pane>
        </a-tabs>
      </a-col>
      <a-col :md="12" :xs="24">
        <a-form :model="form" layout="inline">
          <a-form-item
            field="language"
            label="编程语言"
            style="min-width: 240px"
          >
            <a-select
              v-model="form.language"
              :style="{ width: '320px' }"
              placeholder="选择编程语言"
            >
              <a-option value="java">java</a-option>
              <a-option value="cpp">cpp</a-option>
              <a-option value="go">go</a-option>
              <a-option value="python">python</a-option>
            </a-select>
          </a-form-item>
        </a-form>
        <CodeEditor
          :value="form.code as string"
          :language="form.language"
          :handle-change="changeCode"
        />
        <a-divider size="0" />
        <a-button type="dashed" style="min-width: 200px" @click="doTest">
          运行测试
        </a-button>
        <a-button
          type="primary"
          style="min-width: 200px; float: right"
          @click="doSubmit"
        >
          提交代码
        </a-button>
        <dir />
        <a-space direction="vertical" size="large" :style="{ width: '600px' }">
          <a-form :model="inputList">
            <a-form-item
              v-for="(input, index) of inputList"
              :key="index"
              no-style
            >
              <a-space direction="vertical" style="min-width: 640px">
                <a-form-item
                  :field="`inputList[${index}]`"
                  :label="`输入用例-${index}`"
                  :key="index"
                >
                  <a-textarea
                    v-model="inputList[index]"
                    placeholder="请输入测试输入用例"
                  />

                  <a-button
                    status="danger"
                    @click="handleDelete"
                    style="float: right"
                  >
                    -
                  </a-button>
                </a-form-item>
              </a-space>
            </a-form-item>
            <div style="margin-top: 32px">
              <a-button @click="handleAdd" type="outline" status="success"
                >+
              </a-button>
            </div>
          </a-form>
          <template v-if="testError.errorMessage.length === 0">
            <a-form :model="outputList">
              <a-form-item
                v-for="(output, index) of outputList"
                :key="index"
                no-style
              >
                <a-space direction="vertical" style="min-width: 640px">
                  <a-form-item
                    :field="`outputList[${index}]`"
                    :label="`输出-${index}`"
                    :key="index"
                  >
                    <a-textarea
                      style="white-space: pre-wrap"
                      v-model="outputList[index]"
                    ></a-textarea>
                  </a-form-item>
                </a-space>
              </a-form-item>
            </a-form>
          </template>
          <template v-else>
            <a-form :model="testError">
              <a-form-item no-style>
                <a-space direction="vertical" style="min-width: 640px">
                  <a-form-item :label="`错误`">
                    <a-card :title="testError.judgeInfoStatus">
                      <a-textarea
                        style="white-space: pre-wrap; width: 500px"
                        v-model="testError.errorMessage"
                      ></a-textarea>
                    </a-card>
                  </a-form-item>
                </a-space>
              </a-form-item>
            </a-form>
          </template>
        </a-space>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {
  onMounted,
  ref,
  withDefaults,
  defineProps,
  watch,
  toRaw,
  computed,
} from "vue";
import message from "@arco-design/web-vue/es/message";
import CodeEditor from "@/components/CodeEditor.vue";
import MdViewer from "@/components/MdViewer.vue";
import {
  QuestionControllerService,
  QuestionSubmitAddRequest,
  QuestionSubmitControllerService,
  QuestionVO,
} from "../../../generated";
import { useRoute } from "vue-router";
import * as monaco from "monaco-editor";
import { routes } from "@/router/routes";
import checkAccess from "@/access/checkAccess";
import { useFormItem } from "@arco-design/web-vue";

interface Props {
  id: string;
}

const props = withDefaults(defineProps<Props>(), {
  id: () => useRoute().params.id as any, //获取到url中路径内的参数
});

const question = ref<QuestionVO>();

const loadData = async () => {
  const res = await QuestionControllerService.getQuestionVoByIdUsingGet(
    props.id as any
  );
  if (res.code === 0) {
    question.value = res.data;
  } else {
    message.error("加载失败，" + res.message);
  }
};

const form = ref<QuestionSubmitAddRequest>({
  language: "java",
  code:
    "import java.util.Scanner;\n" +
    "\n" +
    "public class Main {\n" +
    "    public static void main(String[] args) {\n" +
    "        Scanner in = new Scanner(System.in);\n" +
    "\n" +
    "        int a = in.nextInt();\n" +
    "        int b = in.nextInt();\n" +
    "\n" +
    "        System.out.println(a + b);\n" +
    "    }\n" +
    "}",
});

let inputList = ref([""]);
let outputList = ref([]);
let testError = ref({
  judgeInfoStatus: "", // 执行状态
  errorMessage: "", // 错误信息
});

/**
 * 新增判题用例
 */
const handleAdd = () => {
  inputList.value.push("");
};

/**
 * 删除判题用例
 */
const handleDelete = () => {
  inputList.value.pop();
};

/**
 * 运行测试
 */
const doTest = async () => {
  if (!question.value?.id) {
    return;
  }

  const res = await QuestionSubmitControllerService.doQuestionTestUsingPost({
    inputList: inputList.value,
    ...form.value,
    questionId: question.value.id,
  });

  if (res.code === 0) {
    outputList.value = res.data?.outputList ?? [];
    testError.value.judgeInfoStatus = res.data?.judgeInfoStatus ?? "";
    testError.value.errorMessage = res.data?.judgeInfo?.errorMessage ?? "";
  }
  console.log("input", inputList);
  console.log("output", outputList);
  console.log("testError", testError);
};

/**
 * 提交代码
 */
const doSubmit = async () => {
  if (!question.value?.id) {
    return;
  }

  const res = await QuestionSubmitControllerService.doQuestionSubmitUsingPost({
    ...form.value,
    questionId: question.value.id,
  });
  if (res.code === 0) {
    message.success("提交成功");
  } else {
    message.error("提交失败," + res.message);
  }
};

watch(
  () => form.value.language,
  () => {
    if (form.value.language === "java") {
      form.value.code =
        "import java.util.Scanner;\n" +
        "\n" +
        "public class Main {\n" +
        "    public static void main(String[] args) {\n" +
        "        Scanner in = new Scanner(System.in);\n" +
        "\n" +
        "        int a = in.nextInt();\n" +
        "        int b = in.nextInt();\n" +
        "\n" +
        "        System.out.println(a + b);\n" +
        "    }\n" +
        "}";
    } else if (form.value.language === "cpp") {
      form.value.code =
        "#include <iostream>\n" +
        "\n" +
        "using namespace std;\n" +
        "\n" +
        "int main(int argc, char *argv[]){\n" +
        "    int a, b;\n" +
        "    cin >> a >> b;\n" +
        "    cout << a + b;\n" +
        "    return 0;\n" +
        "}";
    } else if (form.value.language === "go") {
      form.value.code =
        "package main\n" +
        "\n" +
        "import (\n" +
        '\t"fmt"\n' +
        ")\n" +
        "\n" +
        "func main() {\n" +
        "\tvar a, b int\n" +
        "\tfmt.Scan(&a, &b)\n" +
        "\tfmt.Println(a + b)\n" +
        "}\n";
    } else if (form.value.language == "python") {
      form.value.code =
        "a, b=map(int, input().split()) #input()会将输入的内容自动转换为字符串，所以需要用int进行类型转换，spilt（）可以进行分割，将a,b分割开来  \n" +
        "print(a + b)";
    } else {
      form.value.code = "";
    }
  }
);
/**
 * 页面加载时，请求数据
 */
onMounted(() => {
  loadData();
});

const changeCode = (value: string) => {
  form.value.code = value;
};
</script>

<style>
#viewQuestionView {
  max-width: 1400px;
  margin: 0 auto;
}

#viewQuestionView .arco-space-horizontal .arco-space-item {
  margin-bottom: 0 !important;
}
</style>
