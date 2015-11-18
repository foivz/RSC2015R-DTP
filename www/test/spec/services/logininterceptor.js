'use strict';

describe('Service: loginInterceptor', function () {

  // load the service's module
  beforeEach(module('webAngularTemplateApp'));

  // instantiate service
  var loginInterceptor;
  beforeEach(inject(function (_loginInterceptor_) {
    loginInterceptor = _loginInterceptor_;
  }));

  it('should do something', function () {
    expect(!!loginInterceptor).toBe(true);
  });

});
