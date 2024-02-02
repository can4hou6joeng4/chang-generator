// @ts-ignore
/* eslint-disable */
import { request } from "@umijs/max";

/** addGenerator POST /generator/add */
export async function addGeneratorUsingPost(
  body: API.GeneratorAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/generator/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** deleteGenerator POST /generator/delete */
export async function deleteGeneratorUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/generator/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** downloadGeneratorById GET /generator/download */
export async function downloadGeneratorByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadGeneratorByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<any>("/generator/download", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** editGenerator POST /generator/edit */
export async function editGeneratorUsingPost(
  body: API.GeneratorEditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/generator/edit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** getGeneratorVOById GET /generator/get/vo */
export async function getGeneratorVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getGeneratorVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseGeneratorVO_>("/generator/get/vo", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listGeneratorByPage POST /generator/list/page */
export async function listGeneratorByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageGenerator_>("/generator/list/page", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** listGeneratorVOByPage POST /generator/list/page/vo */
export async function listGeneratorVoByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageGeneratorVO_>("/generator/list/page/vo", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** listMyGeneratorVOByPage POST /generator/my/list/page/vo */
export async function listMyGeneratorVoByPageUsingPost(
  body: API.GeneratorQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageGeneratorVO_>(
    "/generator/my/list/page/vo",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** updateGenerator POST /generator/update */
export async function updateGeneratorUsingPost(
  body: API.GeneratorUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/generator/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
