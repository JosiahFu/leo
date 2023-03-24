/*eslint-disable block-scoped-var, id-length, no-control-regex, no-magic-numbers, no-prototype-builtins, no-redeclare, no-shadow, no-var, sort-vars*/
'use strict';

var $protobuf = require('protobufjs/minimal');

// Common aliases
var $Reader = $protobuf.Reader,
  $Writer = $protobuf.Writer,
  $util = $protobuf.util;

// Exported root namespace
var $root = $protobuf.roots['default'] || ($protobuf.roots['default'] = {});

$root.greeter = (function () {
  /**
   * Namespace greeter.
   * @exports greeter
   * @namespace
   */
  var greeter = {};

  greeter.Greeter = (function () {
    /**
     * Constructs a new Greeter service.
     * @memberof greeter
     * @classdesc Represents a Greeter
     * @extends $protobuf.rpc.Service
     * @constructor
     * @param {$protobuf.RPCImpl} rpcImpl RPC implementation
     * @param {boolean} [requestDelimited=false] Whether requests are length-delimited
     * @param {boolean} [responseDelimited=false] Whether responses are length-delimited
     */
    function Greeter(rpcImpl, requestDelimited, responseDelimited) {
      $protobuf.rpc.Service.call(
        this,
        rpcImpl,
        requestDelimited,
        responseDelimited
      );
    }

    (Greeter.prototype = Object.create(
      $protobuf.rpc.Service.prototype
    )).constructor = Greeter;

    /**
     * Creates new Greeter service using the specified rpc implementation.
     * @function create
     * @memberof greeter.Greeter
     * @static
     * @param {$protobuf.RPCImpl} rpcImpl RPC implementation
     * @param {boolean} [requestDelimited=false] Whether requests are length-delimited
     * @param {boolean} [responseDelimited=false] Whether responses are length-delimited
     * @returns {Greeter} RPC service. Useful where requests and/or responses are streamed.
     */
    Greeter.create = function create(
      rpcImpl,
      requestDelimited,
      responseDelimited
    ) {
      return new this(rpcImpl, requestDelimited, responseDelimited);
    };

    /**
     * Callback as used by {@link greeter.Greeter#sayHello}.
     * @memberof greeter.Greeter
     * @typedef SayHelloCallback
     * @type {function}
     * @param {Error|null} error Error, if any
     * @param {greeter.HelloReply} [response] HelloReply
     */

    /**
     * Calls SayHello.
     * @function sayHello
     * @memberof greeter.Greeter
     * @instance
     * @param {greeter.IHelloRequest} request HelloRequest message or plain object
     * @param {greeter.Greeter.SayHelloCallback} callback Node-style callback called with the error, if any, and HelloReply
     * @returns {undefined}
     * @variation 1
     */
    Object.defineProperty(
      (Greeter.prototype.sayHello = function sayHello(request, callback) {
        return this.rpcCall(
          sayHello,
          $root.greeter.HelloRequest,
          $root.greeter.HelloReply,
          request,
          callback
        );
      }),
      'name',
      {value: 'SayHello'}
    );

    /**
     * Calls SayHello.
     * @function sayHello
     * @memberof greeter.Greeter
     * @instance
     * @param {greeter.IHelloRequest} request HelloRequest message or plain object
     * @returns {Promise<greeter.HelloReply>} Promise
     * @variation 2
     */

    /**
     * Callback as used by {@link greeter.Greeter#sayHelloStreamReply}.
     * @memberof greeter.Greeter
     * @typedef SayHelloStreamReplyCallback
     * @type {function}
     * @param {Error|null} error Error, if any
     * @param {greeter.HelloReply} [response] HelloReply
     */

    /**
     * Calls SayHelloStreamReply.
     * @function sayHelloStreamReply
     * @memberof greeter.Greeter
     * @instance
     * @param {greeter.IHelloRequest} request HelloRequest message or plain object
     * @param {greeter.Greeter.SayHelloStreamReplyCallback} callback Node-style callback called with the error, if any, and HelloReply
     * @returns {undefined}
     * @variation 1
     */
    Object.defineProperty(
      (Greeter.prototype.sayHelloStreamReply = function sayHelloStreamReply(
        request,
        callback
      ) {
        return this.rpcCall(
          sayHelloStreamReply,
          $root.greeter.HelloRequest,
          $root.greeter.HelloReply,
          request,
          callback
        );
      }),
      'name',
      {value: 'SayHelloStreamReply'}
    );

    /**
     * Calls SayHelloStreamReply.
     * @function sayHelloStreamReply
     * @memberof greeter.Greeter
     * @instance
     * @param {greeter.IHelloRequest} request HelloRequest message or plain object
     * @returns {Promise<greeter.HelloReply>} Promise
     * @variation 2
     */

    return Greeter;
  })();

  greeter.HelloRequest = (function () {
    /**
     * Properties of a HelloRequest.
     * @memberof greeter
     * @interface IHelloRequest
     * @property {string|null} [greeterName] HelloRequest greeterName
     */

    /**
     * Constructs a new HelloRequest.
     * @memberof greeter
     * @classdesc Represents a HelloRequest.
     * @implements IHelloRequest
     * @constructor
     * @param {greeter.IHelloRequest=} [properties] Properties to set
     */
    function HelloRequest(properties) {
      if (properties)
        for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
          if (properties[keys[i]] != null) this[keys[i]] = properties[keys[i]];
    }

    /**
     * HelloRequest greeterName.
     * @member {string|null|undefined} greeterName
     * @memberof greeter.HelloRequest
     * @instance
     */
    HelloRequest.prototype.greeterName = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * HelloRequest _greeterName.
     * @member {"greeterName"|undefined} _greeterName
     * @memberof greeter.HelloRequest
     * @instance
     */
    Object.defineProperty(HelloRequest.prototype, '_greeterName', {
      get: $util.oneOfGetter(($oneOfFields = ['greeterName'])),
      set: $util.oneOfSetter($oneOfFields),
    });

    /**
     * Creates a new HelloRequest instance using the specified properties.
     * @function create
     * @memberof greeter.HelloRequest
     * @static
     * @param {greeter.IHelloRequest=} [properties] Properties to set
     * @returns {greeter.HelloRequest} HelloRequest instance
     */
    HelloRequest.create = function create(properties) {
      return new HelloRequest(properties);
    };

    /**
     * Encodes the specified HelloRequest message. Does not implicitly {@link greeter.HelloRequest.verify|verify} messages.
     * @function encode
     * @memberof greeter.HelloRequest
     * @static
     * @param {greeter.IHelloRequest} message HelloRequest message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HelloRequest.encode = function encode(message, writer) {
      if (!writer) writer = $Writer.create();
      if (
        message.greeterName != null &&
        Object.hasOwnProperty.call(message, 'greeterName')
      )
        writer.uint32(/* id 1, wireType 2 =*/ 10).string(message.greeterName);
      return writer;
    };

    /**
     * Encodes the specified HelloRequest message, length delimited. Does not implicitly {@link greeter.HelloRequest.verify|verify} messages.
     * @function encodeDelimited
     * @memberof greeter.HelloRequest
     * @static
     * @param {greeter.IHelloRequest} message HelloRequest message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HelloRequest.encodeDelimited = function encodeDelimited(message, writer) {
      return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a HelloRequest message from the specified reader or buffer.
     * @function decode
     * @memberof greeter.HelloRequest
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {greeter.HelloRequest} HelloRequest
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HelloRequest.decode = function decode(reader, length) {
      if (!(reader instanceof $Reader)) reader = $Reader.create(reader);
      var end = length === undefined ? reader.len : reader.pos + length,
        message = new $root.greeter.HelloRequest();
      while (reader.pos < end) {
        var tag = reader.uint32();
        switch (tag >>> 3) {
          case 1: {
            message.greeterName = reader.string();
            break;
          }
          default:
            reader.skipType(tag & 7);
            break;
        }
      }
      return message;
    };

    /**
     * Decodes a HelloRequest message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof greeter.HelloRequest
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {greeter.HelloRequest} HelloRequest
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HelloRequest.decodeDelimited = function decodeDelimited(reader) {
      if (!(reader instanceof $Reader)) reader = new $Reader(reader);
      return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a HelloRequest message.
     * @function verify
     * @memberof greeter.HelloRequest
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    HelloRequest.verify = function verify(message) {
      if (typeof message !== 'object' || message === null)
        return 'object expected';
      var properties = {};
      if (
        message.greeterName != null &&
        message.hasOwnProperty('greeterName')
      ) {
        properties._greeterName = 1;
        if (!$util.isString(message.greeterName))
          return 'greeterName: string expected';
      }
      return null;
    };

    /**
     * Creates a HelloRequest message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof greeter.HelloRequest
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {greeter.HelloRequest} HelloRequest
     */
    HelloRequest.fromObject = function fromObject(object) {
      if (object instanceof $root.greeter.HelloRequest) return object;
      var message = new $root.greeter.HelloRequest();
      if (object.greeterName != null)
        message.greeterName = String(object.greeterName);
      return message;
    };

    /**
     * Creates a plain object from a HelloRequest message. Also converts values to other types if specified.
     * @function toObject
     * @memberof greeter.HelloRequest
     * @static
     * @param {greeter.HelloRequest} message HelloRequest
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    HelloRequest.toObject = function toObject(message, options) {
      if (!options) options = {};
      var object = {};
      if (
        message.greeterName != null &&
        message.hasOwnProperty('greeterName')
      ) {
        object.greeterName = message.greeterName;
        if (options.oneofs) object._greeterName = 'greeterName';
      }
      return object;
    };

    /**
     * Converts this HelloRequest to JSON.
     * @function toJSON
     * @memberof greeter.HelloRequest
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    HelloRequest.prototype.toJSON = function toJSON() {
      return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    /**
     * Gets the default type url for HelloRequest
     * @function getTypeUrl
     * @memberof greeter.HelloRequest
     * @static
     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
     * @returns {string} The default type url
     */
    HelloRequest.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
      if (typeUrlPrefix === undefined) {
        typeUrlPrefix = 'type.googleapis.com';
      }
      return typeUrlPrefix + '/greeter.HelloRequest';
    };

    return HelloRequest;
  })();

  greeter.HelloReply = (function () {
    /**
     * Properties of a HelloReply.
     * @memberof greeter
     * @interface IHelloReply
     * @property {string|null} [replyMessage] HelloReply replyMessage
     */

    /**
     * Constructs a new HelloReply.
     * @memberof greeter
     * @classdesc Represents a HelloReply.
     * @implements IHelloReply
     * @constructor
     * @param {greeter.IHelloReply=} [properties] Properties to set
     */
    function HelloReply(properties) {
      if (properties)
        for (var keys = Object.keys(properties), i = 0; i < keys.length; ++i)
          if (properties[keys[i]] != null) this[keys[i]] = properties[keys[i]];
    }

    /**
     * HelloReply replyMessage.
     * @member {string|null|undefined} replyMessage
     * @memberof greeter.HelloReply
     * @instance
     */
    HelloReply.prototype.replyMessage = null;

    // OneOf field names bound to virtual getters and setters
    var $oneOfFields;

    /**
     * HelloReply _replyMessage.
     * @member {"replyMessage"|undefined} _replyMessage
     * @memberof greeter.HelloReply
     * @instance
     */
    Object.defineProperty(HelloReply.prototype, '_replyMessage', {
      get: $util.oneOfGetter(($oneOfFields = ['replyMessage'])),
      set: $util.oneOfSetter($oneOfFields),
    });

    /**
     * Creates a new HelloReply instance using the specified properties.
     * @function create
     * @memberof greeter.HelloReply
     * @static
     * @param {greeter.IHelloReply=} [properties] Properties to set
     * @returns {greeter.HelloReply} HelloReply instance
     */
    HelloReply.create = function create(properties) {
      return new HelloReply(properties);
    };

    /**
     * Encodes the specified HelloReply message. Does not implicitly {@link greeter.HelloReply.verify|verify} messages.
     * @function encode
     * @memberof greeter.HelloReply
     * @static
     * @param {greeter.IHelloReply} message HelloReply message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HelloReply.encode = function encode(message, writer) {
      if (!writer) writer = $Writer.create();
      if (
        message.replyMessage != null &&
        Object.hasOwnProperty.call(message, 'replyMessage')
      )
        writer.uint32(/* id 1, wireType 2 =*/ 10).string(message.replyMessage);
      return writer;
    };

    /**
     * Encodes the specified HelloReply message, length delimited. Does not implicitly {@link greeter.HelloReply.verify|verify} messages.
     * @function encodeDelimited
     * @memberof greeter.HelloReply
     * @static
     * @param {greeter.IHelloReply} message HelloReply message or plain object to encode
     * @param {$protobuf.Writer} [writer] Writer to encode to
     * @returns {$protobuf.Writer} Writer
     */
    HelloReply.encodeDelimited = function encodeDelimited(message, writer) {
      return this.encode(message, writer).ldelim();
    };

    /**
     * Decodes a HelloReply message from the specified reader or buffer.
     * @function decode
     * @memberof greeter.HelloReply
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @param {number} [length] Message length if known beforehand
     * @returns {greeter.HelloReply} HelloReply
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HelloReply.decode = function decode(reader, length) {
      if (!(reader instanceof $Reader)) reader = $Reader.create(reader);
      var end = length === undefined ? reader.len : reader.pos + length,
        message = new $root.greeter.HelloReply();
      while (reader.pos < end) {
        var tag = reader.uint32();
        switch (tag >>> 3) {
          case 1: {
            message.replyMessage = reader.string();
            break;
          }
          default:
            reader.skipType(tag & 7);
            break;
        }
      }
      return message;
    };

    /**
     * Decodes a HelloReply message from the specified reader or buffer, length delimited.
     * @function decodeDelimited
     * @memberof greeter.HelloReply
     * @static
     * @param {$protobuf.Reader|Uint8Array} reader Reader or buffer to decode from
     * @returns {greeter.HelloReply} HelloReply
     * @throws {Error} If the payload is not a reader or valid buffer
     * @throws {$protobuf.util.ProtocolError} If required fields are missing
     */
    HelloReply.decodeDelimited = function decodeDelimited(reader) {
      if (!(reader instanceof $Reader)) reader = new $Reader(reader);
      return this.decode(reader, reader.uint32());
    };

    /**
     * Verifies a HelloReply message.
     * @function verify
     * @memberof greeter.HelloReply
     * @static
     * @param {Object.<string,*>} message Plain object to verify
     * @returns {string|null} `null` if valid, otherwise the reason why it is not
     */
    HelloReply.verify = function verify(message) {
      if (typeof message !== 'object' || message === null)
        return 'object expected';
      var properties = {};
      if (
        message.replyMessage != null &&
        message.hasOwnProperty('replyMessage')
      ) {
        properties._replyMessage = 1;
        if (!$util.isString(message.replyMessage))
          return 'replyMessage: string expected';
      }
      return null;
    };

    /**
     * Creates a HelloReply message from a plain object. Also converts values to their respective internal types.
     * @function fromObject
     * @memberof greeter.HelloReply
     * @static
     * @param {Object.<string,*>} object Plain object
     * @returns {greeter.HelloReply} HelloReply
     */
    HelloReply.fromObject = function fromObject(object) {
      if (object instanceof $root.greeter.HelloReply) return object;
      var message = new $root.greeter.HelloReply();
      if (object.replyMessage != null)
        message.replyMessage = String(object.replyMessage);
      return message;
    };

    /**
     * Creates a plain object from a HelloReply message. Also converts values to other types if specified.
     * @function toObject
     * @memberof greeter.HelloReply
     * @static
     * @param {greeter.HelloReply} message HelloReply
     * @param {$protobuf.IConversionOptions} [options] Conversion options
     * @returns {Object.<string,*>} Plain object
     */
    HelloReply.toObject = function toObject(message, options) {
      if (!options) options = {};
      var object = {};
      if (
        message.replyMessage != null &&
        message.hasOwnProperty('replyMessage')
      ) {
        object.replyMessage = message.replyMessage;
        if (options.oneofs) object._replyMessage = 'replyMessage';
      }
      return object;
    };

    /**
     * Converts this HelloReply to JSON.
     * @function toJSON
     * @memberof greeter.HelloReply
     * @instance
     * @returns {Object.<string,*>} JSON object
     */
    HelloReply.prototype.toJSON = function toJSON() {
      return this.constructor.toObject(this, $protobuf.util.toJSONOptions);
    };

    /**
     * Gets the default type url for HelloReply
     * @function getTypeUrl
     * @memberof greeter.HelloReply
     * @static
     * @param {string} [typeUrlPrefix] your custom typeUrlPrefix(default "type.googleapis.com")
     * @returns {string} The default type url
     */
    HelloReply.getTypeUrl = function getTypeUrl(typeUrlPrefix) {
      if (typeUrlPrefix === undefined) {
        typeUrlPrefix = 'type.googleapis.com';
      }
      return typeUrlPrefix + '/greeter.HelloReply';
    };

    return HelloReply;
  })();

  return greeter;
})();

module.exports = $root;
