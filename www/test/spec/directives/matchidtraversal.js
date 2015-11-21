'use strict';

describe('Directive: matchIdTraversal', function () {

  // load the directive's module
  beforeEach(module('webAngularTemplateApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<match-id-traversal></match-id-traversal>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the matchIdTraversal directive');
  }));
});
