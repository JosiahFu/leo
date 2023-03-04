import {Message, Method, rpc, RPCImpl, RPCImplCallback} from 'protobufjs';

export * from './protos/protobuf-js';

/**
 * The below code is tuned for development. It converts request protos to JSON
 * and response JSON back to protos. For production, we'd want to use the
 * encoded byte versions of protos. This will involve a different protos-dev.ts
 * file. The selection
 */
export function createService<Service>(
  serviceClass: {
    create: (
      rpcImpl: RPCImpl,
      requestDelimited?: boolean,
      responseDelimited?: boolean
    ) => Service;
  },
  serviceName: string
): Service {
  const pathPrefix = `/api/protos/${serviceName}/`;

  const rpcImpl: RPCImpl = function (
    method: Method | rpc.ServiceMethod<Message<{}>, Message<{}>>,
    requestData: Uint8Array,
    callback: RPCImplCallback
  ) {
    const currentUrl = new URL(window.location.href);
    fetch(currentUrl.origin + pathPrefix + method.name, {
      body: requestData,
      cache: 'no-cache',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/x-protobuf',
        'Response-Type': 'application/x-protobuf',
      },
      method: 'POST',
      mode: 'same-origin',
      redirect: 'follow',
    })
      .then((response: Response) => checkResponse(response))
      .then((response: Response) => response.arrayBuffer())
      .then((buffer: ArrayBuffer) => new Uint8Array(buffer))
      .then((array: Uint8Array) => callback(null, array))
      .catch(error => callback(new Error(JSON.stringify(error))));
  };

  return serviceClass.create(rpcImpl, false, false);
}

function checkResponse(response: Response): Response {
  if (!response.ok) {
    throw new Error(`HTTP ${response.status} - ${response.statusText}`);
  } else if (!response.body) {
    throw new Error(`HTTP ${response.status} - Response is missing its body`);
  }
  return response;
}
