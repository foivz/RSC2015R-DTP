'use strict';

describe('Service: action', function () {

  // load the service's module
  beforeEach(module('webAngularTemplateApp'));

  // instantiate service
  var action;
  beforeEach(inject(function (_action_) {
    action = _action_;
  }));

  it('should do something', function () {
    expect(!!action).toBe(true);
  });

});
