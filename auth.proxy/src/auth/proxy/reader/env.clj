(ns auth.proxy.reader.env
  "see: https://github.com/duct-framework/core/blob/master/src/duct/core/env.clj")

(defmulti coerce
  "Coerce a value to the type referenced by a symbol. By default supports
  `Int` and `Str`."
  (fn [x type] type))

(defmethod coerce 'Int [x _]
  (Long/parseLong x))

(defmethod coerce 'Str [x _]
  (str x))

(defn env
  "Resolve an environment variable by name. Optionally accepts a type for
  coercion, and a keyword option, `:or`, that provides a default value if the
  environment variable is missing.
  The arguments may optionally be wrapped in a vector. This is to support
  their use in [[duct.core/read-config]]. For example:
      {:port #duct/env [\"PORT\" Int :or 3000]}"
  ([name]
   (if (vector? name)
     (apply env name)
     (System/getenv name)))
  ([name type & options]
   (if (keyword? type)
     (apply env name 'Str type options)
     (let [{default :or} options]
       (-> (System/getenv name)
           (some-> (coerce type))
           (or default))))))
