'use strict';

describe('Controller: SpectatorCtrl', function () {

  // load the controller's module
  beforeEach(module('webAngularTemplateApp'));

  var SpectatorCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SpectatorCtrl = $controller('SpectatorCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(SpectatorCtrl.awesomeThings.length).toBe(3);
  });
});
