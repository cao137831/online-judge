/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_Page_MyQuestionStatus_ } from "../models/BaseResponse_Page_MyQuestionStatus_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { MyQuestionStatusAddRequest } from "../models/MyQuestionStatusAddRequest";
import type { MyQuestionStatusQueryRequest } from "../models/MyQuestionStatusQueryRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class MyQuestionStatusControllerService {
  /**
   * add
   * @param myQuestionStatusAddRequest myQuestionStatusAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addUsingPost1(
    myQuestionStatusAddRequest: MyQuestionStatusAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/my_questions",
      body: myQuestionStatusAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * remove
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @throws ApiError
   */
  public static removeUsingDelete(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_> {
    return __request(OpenAPI, {
      method: "DELETE",
      url: "/api/my_questions",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
      },
    });
  }

  /**
   * listByPage
   * @param myQuestionStatusQueryRequest myQuestionStatusQueryRequest
   * @returns BaseResponse_Page_MyQuestionStatus_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listByPageUsingPost1(
    myQuestionStatusQueryRequest: MyQuestionStatusQueryRequest
  ): CancelablePromise<BaseResponse_Page_MyQuestionStatus_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/my_questions/list/page",
      body: myQuestionStatusQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * listByAdmin
   * @param myQuestionStatusQueryRequest myQuestionStatusQueryRequest
   * @returns BaseResponse_Page_MyQuestionStatus_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listByAdminUsingPost(
    myQuestionStatusQueryRequest: MyQuestionStatusQueryRequest
  ): CancelablePromise<BaseResponse_Page_MyQuestionStatus_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/my_questions/list/page/admin",
      body: myQuestionStatusQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
