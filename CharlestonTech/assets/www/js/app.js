// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
angular.module('starter', ['ionic'])

.run(function($ionicPlatform) {

  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if(window.StatusBar) {
      StatusBar.styleDefault();
    }
  });
})
.config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('list', {
            url: "/",
            templateUrl: 'list.html',
            controller: 'masterCtrl'
        })
        .state('details', {
            url: "/details/:id",
            templateUrl: 'details.html',
            controller: 'detailsCtrl'
        });

        $urlRouterProvider.otherwise("/");
}])
.factory('EventService', ['$http', function($http) {
    var events = [];
    return {
        GetEvents: function() {
            return $http.get("https://fierce-caverns-8423.herokuapp.com/api/events")
                .then(function(response) {
                    events = response.data;
                    return response.data;
                });
        },
        GetEvent: function(eventId) {
            for (i=0; i<events.length;i++) {
                if(events[i]._id == eventId){
                    return events[i];
                }
            }
        }
    }
}])
.controller("masterCtrl", ['EventService', '$scope', '$state', function(EventService, $scope, $state) {
    EventService.GetEvents().then(function(events) {
        var upcoming = [];
        var past = [];
        var now = new Date();

        for (i=0; i<events.length;i++) {
            events[i].date = new Date(events[i].date);
            if (events[i].date > now) {
                upcoming.push(events[i]);
            } else {
                past.push(events[i]);
            }
        }
        console.log(upcoming);
        $scope.events = upcoming;
        $scope.past = past;

        $scope.changeState = function(eventId) {
            console.log(eventId);
            $state.go('details', {id: eventId});
        }
    });
}])
.controller("detailsCtrl", ['$stateParams', 'EventService', '$scope', function($stateParams, EventService, $scope) {
    var eventId = $stateParams.id;
    $scope.event = EventService.GetEvent(eventId)
}])
