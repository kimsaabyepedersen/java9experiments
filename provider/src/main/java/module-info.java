module provider {
    exports api;
    provides api.MyApi
            with implementation.MyApiImpl;
}