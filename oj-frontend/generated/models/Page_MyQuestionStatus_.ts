/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { MyQuestionStatus } from "./MyQuestionStatus";
import type { OrderItem } from "./OrderItem";

export type Page_MyQuestionStatus_ = {
  countId?: string;
  current?: number;
  maxLimit?: number;
  optimizeCountSql?: boolean;
  orders?: Array<OrderItem>;
  pages?: number;
  records?: Array<MyQuestionStatus>;
  searchCount?: boolean;
  size?: number;
  total?: number;
};
