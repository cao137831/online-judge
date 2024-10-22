/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_Page_MyHistoryRecord_ } from "../models/BaseResponse_Page_MyHistoryRecord_";
import type { MyHistoryRecordAddRequest } from "../models/MyHistoryRecordAddRequest";
import type { MyHistoryRecordQueryRequest } from "../models/MyHistoryRecordQueryRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class MyHistoryRecordControllerService {
  /**
   * add
   * @param myHistoryRecordAddRequest myHistoryRecordAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addUsingPost(
    myHistoryRecordAddRequest: MyHistoryRecordAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/history_records",
      body: myHistoryRecordAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * listByPage
   * @param myHistoryRecordQueryRequest myHistoryRecordQueryRequest
   * @returns BaseResponse_Page_MyHistoryRecord_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listByPageUsingPost(
    myHistoryRecordQueryRequest: MyHistoryRecordQueryRequest
  ): CancelablePromise<BaseResponse_Page_MyHistoryRecord_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/history_records/list/page",
      body: myHistoryRecordQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
