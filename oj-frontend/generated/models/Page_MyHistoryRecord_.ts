/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { MyHistoryRecord } from "./MyHistoryRecord";
import type { OrderItem } from "./OrderItem";

export type Page_MyHistoryRecord_ = {
  countId?: string;
  current?: number;
  maxLimit?: number;
  optimizeCountSql?: boolean;
  orders?: Array<OrderItem>;
  pages?: number;
  records?: Array<MyHistoryRecord>;
  searchCount?: boolean;
  size?: number;
  total?: number;
};
