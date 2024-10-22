/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_long_ } from "../models/BaseResponse_long_";
import type { BaseResponse_Page_User_ } from "../models/BaseResponse_Page_User_";
import type { BaseResponse_Page_UserVO_ } from "../models/BaseResponse_Page_UserVO_";
import type { BaseResponse_User_ } from "../models/BaseResponse_User_";
import type { BaseResponse_UserVO_ } from "../models/BaseResponse_UserVO_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { UserAddRequest } from "../models/UserAddRequest";
import type { UserLoginRequest } from "../models/UserLoginRequest";
import type { UserQueryRequest } from "../models/UserQueryRequest";
import type { UserRegisterRequest } from "../models/UserRegisterRequest";
import type { UserUpdateMyRequest } from "../models/UserUpdateMyRequest";
import type { UserUpdateRequest } from "../models/UserUpdateRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class UserControllerService {
  /**
   * add
   * @param userAddRequest userAddRequest
   * @returns BaseResponse_long_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addUsingPost3(
    userAddRequest: UserAddRequest
  ): CancelablePromise<BaseResponse_long_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/users",
      body: userAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * update
   * @param userUpdateRequest userUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateUsingPut1(
    userUpdateRequest: UserUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "PUT",
      url: "/api/users",
      body: userUpdateRequest,
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
  public static removeUsingDelete2(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_> {
    return __request(OpenAPI, {
      method: "DELETE",
      url: "/api/users",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
      },
    });
  }

  /**
   * listUserByPage
   * @param userQueryRequest userQueryRequest
   * @returns BaseResponse_Page_User_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listUserByPageUsingPost(
    userQueryRequest: UserQueryRequest
  ): CancelablePromise<BaseResponse_Page_User_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/users/list/page",
      body: userQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * listUserVoByPage
   * @param userQueryRequest userQueryRequest
   * @returns BaseResponse_Page_UserVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static listUserVoByPageUsingPost(
    userQueryRequest: UserQueryRequest
  ): CancelablePromise<BaseResponse_Page_UserVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/users/list/page/vo",
      body: userQueryRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * login
   * @param userLoginRequest userLoginRequest
   * @returns BaseResponse_UserVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static loginUsingPost(
    userLoginRequest: UserLoginRequest
  ): CancelablePromise<BaseResponse_UserVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/users/login",
      body: userLoginRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getLoginUser
   * @returns BaseResponse_UserVO_ OK
   * @throws ApiError
   */
  public static getLoginUserUsingGet(): CancelablePromise<BaseResponse_UserVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/users/login_user",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * logout
   * @returns BaseResponse_boolean_ OK
   * @throws ApiError
   */
  public static logoutUsingGet(): CancelablePromise<BaseResponse_boolean_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/users/logout",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateMyUser
   * @param userUpdateMyRequest userUpdateMyRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateMyUserUsingPut(
    userUpdateMyRequest: UserUpdateMyRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "PUT",
      url: "/api/users/my",
      body: userUpdateMyRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * register
   * @param userRegisterRequest userRegisterRequest
   * @returns BaseResponse_long_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static registerUsingPost(
    userRegisterRequest: UserRegisterRequest
  ): CancelablePromise<BaseResponse_long_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/users/register",
      body: userRegisterRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getUserById
   * @param id id
   * @returns BaseResponse_User_ OK
   * @throws ApiError
   */
  public static getUserByIdUsingGet(
    id: number
  ): CancelablePromise<BaseResponse_User_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/users/{id}",
      path: {
        id: id,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getUserVoById
   * @param id id
   * @returns BaseResponse_UserVO_ OK
   * @throws ApiError
   */
  public static getUserVoByIdUsingGet(
    id: number
  ): CancelablePromise<BaseResponse_UserVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/users/{id}/vo",
      path: {
        id: id,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
