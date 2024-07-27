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
                  {{ question.judgeConfig.timeLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="内存限制">
                  {{ question.judgeConfig.memoryLimit ?? 0 }}
                </a-descriptions-item>
                <a-descriptions-item label="堆栈限制">
                  {{ question.judgeConfig.stackLimit ?? 0 }}
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
        <a-button type="primary" style="min-width: 200px" @click="doSubmit">
          提交代码
        </a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, withDefaults, defineProps, watch, toRaw } from "vue";
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
    // //console.log(form.value);
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
