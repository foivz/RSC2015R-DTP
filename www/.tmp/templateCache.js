angular.module('webAngularTemplateApp').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('views/about.html',
    "<div class=\"container\"> <h2>Kontakt lista</h2> <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Dolorum earum, corrupti voluptatem magni, vero suscipit, quos maiores repudiandae minus deleniti architecto fugiat excepturi provident pariatur quisquam odit. A quae, quia!</p> </div>"
  );


  $templateCache.put('views/login.html',
    "<div class=\"container login\"> <div class=\"row\"> <div class=\"text-center\"> <h2>LOGO</h2> </div> <div class=\"form-wrapper col-md-8 col-md-offset-2\"> <hr> <form ng-submit=\"login.CheckUser()\"> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> </div> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-lock\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.password\" type=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> </div> </div> <div ng-show=\"login.errorMessage\" class=\"alert alert-danger\" role=\"alert\">{{login.errorMessage}}</div> <button class=\"btn btn-primary btn-block\">Sign in</button> <div class=\"text-center\"> <a ng-href=\"#/register\">Don't have an account? <strong>Sign up</strong></a> </div> </form> </div> </div> </div>"
  );


  $templateCache.put('views/main.html',
    "<div class=\"container\"> <h1>Pocetna stranica</h1> <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ea eius obcaecati suscipit maxime ducimus! Qui illum adipisci accusamus consectetur magnam cumque quo voluptatem dolorem perferendis libero ullam possimus atque maxime.</p> </div>"
  );


  $templateCache.put('views/register.html',
    "<div class=\"container registration\"> <div class=\"row\"> <div class=\"text-center\" style=\"margin-top: 10%\"> <h2>LOGO</h2> </div> <form ng-submit=\"register.GetCoordinates()\" class=\"form-wrapper col-md-6 col-md-offset-3\" enctype=\"multipart/form-data\"> <hr> <div class=\"form-group\"> <input ng-model=\"register.name\" type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"Name\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.surname\" type=\"text\" name=\"surname\" class=\"form-control\" placeholder=\"Surname\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.email\" type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Email\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> <div class=\"form-group\"> <div class=\"input-group\"> <input ng-model=\"register.password\" type=\"{{register.passwordType}}\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> <div ng-click=\"register.ChangePassVisibility()\" class=\"input-group-addon pass-opacity\"> <span ng-class=\"register.passwordType == 'password' ? 'glyphicon-eye-open':'glyphicon-eye-close'\" class=\"glyphicon\"></span> </div> </div> </div> <button class=\"btn btn-primary btn-block\">Submit</button> <div class=\"text-center\"> <a ng-href=\"#/login\">Back to login</a> </div> </form> </div> </div>"
  );

}]);
