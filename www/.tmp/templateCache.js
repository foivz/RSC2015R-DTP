angular.module('webAngularTemplateApp').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('views/introduction.html',
    "<div class=\"text-center\" ng-controller=\"MatchCtrl as match\"> <div id=\"fullpage\"> <div class=\"section first-section\"> <div class=\"img-container\"> <img src=\"images/logo.png\" alt=\"\" width=\"400px\"> </div> </div> <div class=\"section second-section\"> <div class=\"row\"> <div class=\"col-md-4\"> <img src=\"images/aim.png\" alt=\"\" width=\"200px\"> <h2>AIM FOR VICTORY</h2> </div> <div class=\"col-md-4\"> <img src=\"images/paintbal.png\" alt=\"\" width=\"330px\"> <h2>nešta</h2> </div> <div class=\"col-md-4\"> <img src=\"images/multiplayer.png\" alt=\"\" width=\"200px\"> <h2>PLAY WITH FRIENDS</h2> </div> </div> </div> <div class=\"section third-section\"> <h1>WATCH GAME LIVE</h1> <div class=\"container\"> <table class=\"table\"> <tr class=\"text-center\"> <th>No.</th> <th>Team 1</th> <th>Team 2</th> <th>Map</th> <th>WATCH GAME</th> </tr> <tr class=\"text-left\" ng-repeat=\"match in match.Matches\" ng-show=\"match.ready\"> <td>{{$index+1}}.</td> <td>{{match.team[0].name}}</td> <td>{{match.team[1].name}}</td> <td>{{match.map.name}}</td> <td> <a href=\"#main\"> <span class=\"glyphicon glyphicon-play\" match-id-traversal data-matchid=\"{{match.idGame}}\"> </span> </a> </td> </tr> </table> <a href=\"#/login\"> <h4>JUDGE LOGIN</h4> </a> </div> </div> </div> </div> <script>jQuery(document).ready(function($) {\n" +
    "\t\t$('#fullpage').fullpage();\n" +
    "\t});</script>"
  );


  $templateCache.put('views/login.html',
    "<div class=\"container login\"> <div class=\"row\"> <div class=\"text-center\" style=\"margin-top:10px\"> <img src=\"images/logo.png\" alt=\"\" width=\"300px\"> </div> <div class=\"form-wrapper col-md-8 col-md-offset-2\"> <hr> <form ng-submit=\"login.CheckUser()\"> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-user\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> </div> <div class=\"form-group\"> <div class=\"input-group\"> <div class=\"input-group-addon\"><span class=\"glyphicon glyphicon-lock\" aria-hidden=\"true\"></span></div> <input ng-model=\"login.password\" type=\"password\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> </div> </div> <div ng-show=\"login.errorMessage\" class=\"alert alert-danger\" role=\"alert\">{{login.errorMessage}}</div> <button class=\"btn btn-primary btn-block\">LOGIN</button> <div class=\"text-center\"> <a ng-href=\"#/register\">Don't have an account? <strong>Sign up</strong></a> </div> </form> </div> </div> </div>"
  );


  $templateCache.put('views/main.html',
    "<div class=\"container-fluid\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> </div> <div class=\"mainMatch\"> <div class=\"col-md-6\"> <h2>{{main.team1.idTeam + \" \" + main.team1.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> <th>Ready to rumble</th> </tr> <tr ng-repeat=\"people in main.team1People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> <td> <span class=\"glyphicon glyphicon-ok\" ng-show=\"people.ready\"></span> <span class=\"glyphicon glyphicon-time\" ng-hide=\"people.ready\"></span> </td> </tr> </table> </div> <div class=\"col-md-6\"> <h2>{{main.team2.idTeam + \" \" + main.team2.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> <th>Ready to rumble</th> </tr> <tr ng-repeat=\"people in main.team2People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> <td> <span class=\"glyphicon glyphicon-ok\" ng-show=\"people.ready\"></span> <span class=\"glyphicon glyphicon-time\" ng-hide=\"people.ready\"></span> </td> </tr> </table> </div> <div class=\"container text-center\"> <button ng-click=\"main.StartMatch()\" class=\"btn btn-info\">START</button> </div> <!-- <button class=\"btn btn-default\"><img src=\"images/chip.png\" width=\"30px\" alt=\"\"> Place a bet</button> --> </div>"
  );


  $templateCache.put('views/map.html',
    "<div class=\"container-fluid matchMap\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> <div class=\"container\"> <div class=\"obstacles\"> <img ng-click=\"map.setIcon('flag')\" ng-class=\"{'bordered' : map.icon == 'flag'}\" src=\"images/icon_flag.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('house')\" ng-class=\"{'bordered' : map.icon == 'house'}\" src=\"images/icon_house.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('fence')\" ng-class=\"{'bordered' : map.icon == 'fence'}\" src=\"images/icon_fence.png\" width=\"70px\" alt=\"\"> <img ng-click=\"map.setIcon('woman')\" ng-class=\"{'bordered' : map.icon == 'woman'}\" src=\"images/icon_woman.png\" width=\"70px\" alt=\"\"> </div> <form> <br> <input type=\"text\" class=\"text-center\" ng-model=\"map.mapName\" name=\"username\" placeholder=\"Map name\" required> <br> <br> <br> <br> <div class=\"circle-container\" ng-click=\"map.AddMap()\"> <div class=\"outer-ring\"></div> <div class=\"circle\"> <div class=\"circle-front circle-front-map\"> <p>CREATE MAP</p> </div> <div class=\"circle-back\"> <img class=\"back-logo\" src=\"images/logo2.png\" alt=\"The Elevation Group Logo\"> </div> </div> </div> </form></div> <br>  </div> "
  );


  $templateCache.put('views/match-action.html',
    "<span id=\"time\">05:00</span> <div class=\"container-fluid\"> <div id=\"matchMap\" style=\"width: 100%; height: 400px\"></div> </div> <div class=\"mainMatch\"> <div class=\"col-md-4\"> <h2>{{matchAction.team1.idTeam + \" \" + matchAction.team1.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team1People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>{{matchAction.team2.idTeam + \" \" + matchAction.team2.name}}</h2> <table class=\"table\"> <tr> <th>No.</th> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"people in matchAction.team2People\"> <td>{{$index+1}}</td> <td>{{people.name + \" \" + people.surname}}</td> <td>{{people.kill}}</td> <td>{{people.death}}</td> </tr> </table> </div> <div class=\"col-md-4\"> <h2>Notifications</h2> <table class=\"table\"> <tr> <th>Name</th> <th>Description</th> </tr> <tr> <td></td> </tr> </table> </div> <div class=\"container text-center\"> <button ng-click=\"matchAction.EndGame()\" class=\"btn btn-info\">END</button> </div> <!-- <button class=\"btn btn-default\"><img src=\"images/chip.png\" width=\"30px\" alt=\"\"> Place a bet</button> --> </div>"
  );


  $templateCache.put('views/new-match.html',
    "<div class=\"container-fluid\"> <form> <div class=\"row\"> <div class=\"col-md-12 alphaTeam\"> <div class=\"col-md-6\"> <div class=\"form-group\"> <input type=\"text\" name=\"team1\" ng-model=\"newMatch.team1\" placeholder=\"Team 1 Name\" required> </div> </div> </div> <div class=\"col-md-12 betaTeam\"> <div class=\"col-md-6 pull-right\"> <div class=\"form-group\"> <input type=\"text\" name=\"team2\" ng-model=\"newMatch.team2\" placeholder=\"Team 2 Name\" required> </div> </div> </div> </div> <div class=\"container\"> <div class=\"row MapChoice\"> <h2>Choose map:</h2> <table class=\"table\"> <tr ng-repeat=\"map in newMatch.mapList\"> <td> <div class=\"takeForm\"> <input type=\"radio\" id=\"{{map.name}}\" ng-model=\"newMatch.map\" name=\"name\" value=\"{{$index}}\" required> <label for=\"{{map.name}}\"> {{map.name}} </label> </div> </td> </tr> </table> <!-- <select class=\"form-control\" ng-model=\"newMatch.map\">\n" +
    "\t\t\t\t\t<option value=\"\" disabled selected>Select Map</option>\n" +
    "\t\t\t\t\t<option ng-repeat=\"map in newMatch.mapList\" value=\"{{$index}}\">{{map.name}}</option>\n" +
    "\t\t\t\t</select>\n" +
    "\t\t\t\t<br>\n" +
    " --> <div class=\"form-group timer\" class=\"text-center\"> <input type=\"text\" class=\"text-center\" name=\"timer\" ng-model=\"newMatch.timer\" placeholder=\"Game time (min)\" required> </div> <br> <br> <div class=\"circle-container\" ng-click=\"newMatch.AddMatch()\"> <div class=\"outer-ring\"></div> <div class=\"circle\"> <div class=\"circle-front\"> <p>START</p> </div> <div class=\"circle-back\"> <img class=\"back-logo\" src=\"images/logo2.png\" alt=\"The Elevation Group Logo\"> </div> </div> </div> <div class=\"alert alert-success\" ng-show=\"newMatch.alert\" role=\"alert\"> <span class=\"glyphicon glyphicon-ok\"></span> Match created successfully </div> <br><br> </div> </div> </form> </div>"
  );


  $templateCache.put('views/register.html',
    "<div class=\"container login register\"> <div class=\"row\"> <div class=\"text-center\"> <h2>LOGO</h2> </div> <form ng-submit=\"register.Register()\" class=\"form-wrapper col-md-10 col-md-offset-1\" enctype=\"multipart/form-data\"> <hr> <div class=\"form-group\"> <input ng-model=\"register.name\" type=\"text\" name=\"name\" class=\"form-control\" placeholder=\"Name\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.surname\" type=\"text\" name=\"surname\" class=\"form-control\" placeholder=\"Surname\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.email\" type=\"email\" name=\"email\" class=\"form-control\" placeholder=\"Email\" required> </div> <div class=\"form-group\"> <input ng-model=\"register.username\" type=\"text\" name=\"username\" class=\"form-control\" placeholder=\"Username\" required> </div> <div class=\"form-group\"> <div class=\"input-group\"> <input ng-model=\"register.password\" type=\"{{register.passwordType}}\" name=\"password\" class=\"form-control\" placeholder=\"Password\" required> <div ng-click=\"register.ChangePassVisibility()\" class=\"input-group-addon pass-opacity\"> <span ng-class=\"register.passwordType == 'password' ? 'glyphicon-eye-open':'glyphicon-eye-close'\" class=\"glyphicon\"></span> </div> </div> </div> <button class=\"btn btn-primary btn-block\">SUBMIT</button> </form> </div> </div>"
  );


  $templateCache.put('views/statistic.html',
    "<div class=\"container statistic\"> <br> <br> <div class=\"text-center head\"> <img src=\"images/statistic.png\" alt=\"\" width=\"500px\"> <h1>WINNER IS <span>BLUE</span> TEAM</h1> </div> <br> <br> <h1>Killers and Score</h1> <table class=\"table\"> <tr> <th>Name</th> <th>Kill</th> <th>Death</th> </tr> <tr ng-repeat=\"stat in statistic.stat\"> <td>{{stat.name}}</td> <td>{{stat.kill}}</td> <td>{{stat.death}}</td> </tr> </table> </div>"
  );


  $templateCache.put('views/wrapper.html',
    "<div class=\"menu-container\"> <div class=\"menu-container__icon\" ng-click=\"nav.ToggleNavigation();\" ng-class=\"{'menu-rotate': nav.toggle}\"> <!-- ng-show=\"nav.CheckLogin()\" --> <div class=\"menu-icon menu-icon--top\" ng-class=\"{'menu-top-action': nav.toggle}\"></div> <div class=\"menu-icon menu-icon--middle\" ng-class=\"{'menu-middle-action': nav.toggle}\"></div> <div class=\"menu-icon menu-icon--bottom\" ng-class=\"{'menu-bottom-action': nav.toggle}\"></div> </div> <div class=\"menu-container__app\"> <div class=\"menu-container__app-login\"> <!-- <a href=\"#register\" ng-hide=\"nav.CheckLogin()\">\n" +
    "        <span class=\"glyphicon glyphicon-user\"></span> Register\n" +
    "      </a> --> <a href=\"#login\" ng-hide=\"nav.CheckLogin()\"> <span class=\"glyphicon glyphicon-log-in\"></span> Judge </a> <a href=\"#login\" logout ng-show=\"nav.CheckLogin()\"> <span class=\"glyphicon glyphicon-log-out\"></span> Log out </a> </div> </div> </div> <div class=\"flip-container\"> <div class=\"flip-inner\" ng-class=\"{'flip-inner-action': nav.toggle}\"> <div class=\"front\"> <div class=\"content\"> <div ng-view=\"\"></div> </div> </div> <div class=\"back\"> <nav> <ul> <li><a href=\"#new-match\" ng-click=\"nav.ToggleNavigation();\">NEW MATCH</a></li> <li><a href=\"#map\" ng-click=\"nav.ToggleNavigation();\">MAP</a></li> <li ng-show=\"nav.CheckLogin()\"> <a href=\"#login\" logout> Log out </a> </li> </ul> </nav> </div> <!-- back --> </div> <!-- flip-inner --> </div> <!-- flip-container -->"
  );

}]);