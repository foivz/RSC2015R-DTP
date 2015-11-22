'use strict';

describe('Directive: obstacleIcons', function () {

  // load the directive's module
  beforeEach(module('webAngularTemplateApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<obstacle-icons></obstacle-icons>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the obstacleIcons directive');
  }));
});
