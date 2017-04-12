{
   "helloWorld": {
      "/GET": "HelloWorld#getIndex",
      "/POST": "HelloWorld#tryPost",
      "index": {
        "/GET": "HelloWorld#getIndex"
      },
      "=msg": {
          "/GET": "HelloWorld#getMsg"
      }
  },
  "/GET": "HelloWorld#getIndex",
  "/POST": "HelloWorld#tryPost"
}