/** * Main AngularJS Web Application */
$(document).foundation({
    offcanvas: {
        close_on_click: false
    }
});
var app = angular.module('playground-admin', [ 'ngRoute', 'ngResource', 'angularFileUpload', 'angularSpinner', 'ngCsv']);

app.factory( 'userservice', [ '$resource', '$http', function($resource , $http){
    return new UserService($resource);
}] );



// register the interceptor as a service
app.factory('myHttpInterceptor', function() {
    return {
        // optional method
        'request': function(config) {
            if (localStorage.getItem('currentUser')){
                var user = JSON.parse(localStorage.getItem('currentUser'));
                config.url = config.url + "?userId=" + getUserId() + "&institutionId=" + getInstitutionId();
            }
            return config;
        }
    };
});
app.config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('myHttpInterceptor');
}]);







/** * Configure the Routes */
app.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
        // Pages
        .when("/manage_users", {templateUrl: "partials/manage_users.html", controller: "ManageUserCtrl"})
        .when("/upload_users_success", {templateUrl: "partials/upload_users_authorizations.html", controller: "UploadUserResultsController"})
        .when("/upload_users_failed", {templateUrl: "partials/upload_users_failed.html", controller: "UploadUserResultsController"})
        .when("/submit_auth_success", {templateUrl: "partials/submit_auth_success.html", controller: "PageCtrl"})
        // else 404
        .otherwise("/404", {templateUrl: "partials/404.html", controller: "PageCtrl"});
}]);

app.run(['$rootScope', function($rootScope) {
    $rootScope.$on('$viewContentLoaded', function() {
        loadFoundationJavaScript($rootScope);
        if (localStorage.getItem('currentUser')) {
            $rootScope.currentUser = JSON.parse(localStorage.getItem('currentUser'));
        }
    });
}]);

/** * Controls all other Pages */
app.controller('PageCtrl', function($scope, $routeParams) {
    console.log('PageCtrl')
});


app.controller("ManageUserCtrl", [ '$scope', '$http', 'userservice', function($scope, $http, userservice) {
    //TODO: remove this before integration
    setCurrentUserForDevelopment();


    $scope.sortOrder = 'ASC';
    $scope.sortedBy = 'name';
    $scope.filteredBy = 'All'
    $scope.selectedAuthorities = {};
    userservice.getUsers($scope);

    authorities($scope);

    $scope.toggleAuthority = function(authority) {
        return $scope.selectedAuthorities[authority] = !$scope.selectedAuthorities[authority];
    };

    $scope.toggleSortOrder = function(sortedBy) {
        $scope.sortedBy = sortedBy;
        if(_.isEqual($scope.sortOrder, 'ASC')) {
            $scope.sortOrder = 'DESC';
        } else if (_.isEqual($scope.sortOrder, 'DESC')) {
            $scope.sortOrder = 'ASC';
        }
        userservice.getUsers($scope);
    };

    $scope.checkAuthority = function(authorities, testValue) {
        for (authority in authorities) {
            if(authorities[authority] == testValue)
                return true;
        }
        return false;
    };


    $scope.addUser = function(){
        resetUserDialog();
        $scope.editUserFlag = false;
        $scope.sendEmail = true;
        $( "#addUser" ).click();
    };

    $scope.editUser = function(id){
        resetUserDialog();
        userservice.getUser(id, $scope);
        $scope.editUserFlag = true;
        $scope.sendEmail = false;
        $( "#addUser" ).click();
    };

    $scope.cancelUser = function(){
        resetUserDialog();
        $( "#closeMenu" ).click();
    }

    $scope.getSelectedAuthorities = function() {
        var arr, authority, status, _ref;
        arr = [];
        _ref = $scope.selectedAuthorities;
        for (authority in _ref) {
            status = _ref[authority];
            if (status) {
                arr.push(authority);
            }
        }
        return arr;
    };

    $scope.createNewUser = function(){
        if($scope.getSelectedAuthorities().length == 0){
            $scope.authorityError = "true";
        }else {
            var newuser = { 'firstName': $scope.user.firstName, 'lastName': $scope.user.lastName, 'email': $scope.user.email, 'authorities': $scope.getSelectedAuthorities(),
                'sendEmail': $scope.sendEmail, 'institutionId': getInstitutionId(), 'id': $scope.user.id, 'userInstitutionProfileStatus': $scope.user.userInstitutionProfileStatus};
            userservice.saveUser(newuser, $scope);
            $("#closeMenu").click();
        }
    };

    function resetUserDialog(){
        if($scope.user) {
            $scope.user = null;
            $scope.blurFirstName = "false";
            $scope.blurLastName = "false";
            $scope.blurEmail = "false";
            $scope.authorityError = "false";
            $scope.selectedAuthorities = {};
        }
    }

}]);


app.controller("UploadUserController", [ '$scope', '$upload','$location','userservice', '$rootScope', function($scope, $upload, $location, userservice, $rootScope) {
    $scope.onFileSelect = function($files) {
        //$files: an array of files selected, each file has name, size, and type.
        for (var i = 0; i < $files.length; i++) {
            var file = $files[i];
            $scope.upload = $upload.upload({
                url: 'api/users/upload', //upload.php script, node.js route, or servlet url
                //method: 'POST' or 'PUT',
                //headers: {'header-key': 'header-value'},
                //withCredentials: true,
                data: {},
                file: $files // or list of files ($files) for html5 only
                //fileName: 'doc.jpg' or ['1.jpg', '2.jpg', ...] // to modify the name of the file(s)
                // customize file formData name ('Content-Disposition'), server side file variable name.
                //fileFormDataName: myFile, //or a list of names for multiple files (html5). Default is 'file'
                // customize how data is added to formData. See #40#issuecomment-28612000 for sample code
                //formDataAppender: function(formData, key, val){}
            }).progress(function(evt) {
                console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
            }).success(function(data, status, headers, config) {
                // file is uploaded successfully
                if(data.SUCCESS) {
                    $rootScope.users = data.SUCCESS;
                    $location.path('/upload_users_success').replace();
                }else{
                    $rootScope.users = data.FAILED;
                    $location.path('/upload_users_failed').replace();
                }
            });
            //.error(...)
            //.then(success, error, progress);
            // access or attach event listeners to the underlying XMLHttpRequest.
            //.xhr(function(xhr){xhr.upload.addEventListener(...)})
        }
        /* alternative way of uploading, send the file binary with the file's content-type.
         Could be used to upload files to CouchDB, imgur, etc... html5 FileReader is needed.
         It could also be used to monitor the progress of a normal http post/put request with large data*/
        // $scope.upload = $upload.http({...})  see 88#issuecomment-31366487 for sample code.
    };
}]);

app.controller("UploadUserResultsController", [ '$scope','$rootScope', '$filter', '$location', 'userservice', 'usSpinnerService', function($scope, $rootScope, $filter, $location, userservice, usSpinnerService) {
    $scope.users = $rootScope.users;
    $scope.selectedAuthorities = [];
    $scope.toggleAuthority = function(user, authority) {
        if(user.authorities == null)
            user.authorities=[];

        if(this.confirmed) {
            user.authorities.push(authority);
        }else{
            user.authorities = $filter('filter')(user.authorities, !authority)
        }
    };

    $scope.submitAuthorizations = function() {
        $scope.authorityError = "false";
        $scope.startSpin();
        for (var i = 0, len = $scope.users.length; i < len; i++) {
            if($scope.users[i].authorities == null || $scope.users[i].authorities.length === 0){
                $scope.authorityError = "true";
                $scope.stopSpin();
                break;
            }
            $scope.users[i].institutionId = getInstitutionId()
        }
        if($scope.authorityError === "false") {
            var promise = userservice.sendAuthoriations($scope.users).$promise;
            promise.then(function () {
                    $scope.stopSpin();
                    $location.path('/submit_auth_success').replace();
                }
            )
        }
    };

    $scope.startSpin = function(){
        usSpinnerService.spin('assign-authorizations-spinner');
    };

    $scope.stopSpin = function(){
        usSpinnerService.stop('assign-authorizations-spinner');
    };

    authorities($scope);
}]);




app.directive('leftSideMenu', ['$timeout', 'userservice', function(timer, userservice) {

    function link(scope, elem, attrs, ctrl) {
        function setName() {
            //setCurrentUserForDevelopment();
            scope.currentUserId = getUserId();

            userservice.getUserForMenu(scope.currentUserId, scope);
        }
        timer(setName, 0);
    }

    return {
        link: link,
        scope: true,
        templateUrl: 'templates/menu.html'
    }
}]);


app.directive("emailVerify", function() {
    return {
        require: "ngModel",
        scope: {
            emailVerify: '='
        },
        link: function(scope, element, attrs, ctrl) {
            scope.$watch(function() {
                var combined;

                if (scope.emailVerify || ctrl.$viewValue) {
                    combined = scope.emailVerify + '_' + ctrl.$viewValue;
                }
                return combined;
            }, function(value) {
                if (value) {
                    ctrl.$parsers.unshift(function(viewValue) {
                        var origin = scope.emailVerify;
                        if (origin !== viewValue) {
                            ctrl.$setValidity("emailVerify", false);
                            return undefined;
                        } else {
                            ctrl.$setValidity("emailVerify", true);
                            return viewValue;
                        }
                    });
                }
            });
        }
    };
});

function UserService(resource) {

    this.resource = resource;

    this.saveUser = function ( user, scope) {
        //
        // Save Action Method
        //
        var User = resource('api/users/save');
        var save = User.save(user, function(response){
            user.id=response.id;
        });
        save.$promise.then(function(data){
            getUsers(scope);
        });
        return save;
    }

    this.sendAuthoriations = function ( userArray, scope ) {
        //
        // Save Action Method
        //
        var User = resource('api/users/authorizations');
        return User.save(userArray, function(response){
        });
    }

    this.getUser = function ( id, scope ) {
        //
        // GET Action Method
        //
        var User = resource('api/users/:userId', {userId:'@userId'});
        User.get( {userId:id}, function(user){
            scope.user = user;
        }).$promise.then(function(){
                scope.selectedAuthorities = {};
                angular.forEach(scope.user.authorities, function(authority, index){
                    scope.toggleAuthority(authority);
                });
            });
    }

    this.getUserForMenu = function ( id, scope ) {
        //
        // GET Action Method
        //
        var User = resource('api/users/:userId', {userId:'@userId'});
        User.get( {userId:id}, function(user){
            scope.user = user;
        });
    }

    var getUsers = this.getUsers = function(scope) {
        //
        // Query Action Method
        //
        var Users = resource('api/users');
        Users.query(
            {sortedBy: scope.sortedBy, sortOrder: scope.sortOrder, filteredBy: scope.filteredBy},function(users){
            scope.users = users;
        });
    }
}


function loadFoundationJavaScript(scope){
    //load foundation javascript
    scope.load = function() {
        $('.off-canvas-wrap').removeClass('move-left');
        var toggleConfigMenu = function() {
            $('.right-off-canvas-menu').css('display', 'none');
            $("." + $(this).data("listname")).css('display', 'block');
            $('.off-canvas-wrap').foundation('offcanvas', 'toggle', 'move-left');
        };

        $('.right-off-canvas-toggle').click(toggleConfigMenu);

        $('.close-right-menu').click(function() {
            $('.off-canvas-wrap').foundation('offcanvas', 'toggle', 'move-left');
            return null;
        });

        $('.close-left-menu').click(function() {
            $('.off-canvas-wrap').foundation('offcanvas', 'toggle', 'move-right');
        });

        $('.toggle-panel').click(function() {
            $($(this).attr('href')).toggle();
            return $('.toggle-panel').toggle();
        });
        $('.bar').each(function() {
            return $(this).css('width', $(this).find('span').text() / 4 * 100 + '%').css('opacity', $(this).find('span').text() / 4);
        });
        $('.admin-upload__dd').on('dragover', function() {
            $(this).addClass('hover');
        });
        return $('.admin-upload__dd').on('dragleave', function() {
            $(this).removeClass('hover');
        });
    };
    //don't forget to call the load function
    scope.load();
}

function authorities(scope){
    scope.authorities = ['Observer',
        'Instructor',
        'Admin'
    ];
}

function getInstitutionId(){
    if (localStorage.getItem('currentUser')) {
        var user = JSON.parse(localStorage.getItem('currentUser'));
        return user.currentSchool.school_id;
    }
}
function getUserId() {
    if (localStorage.getItem('currentUser')) {
        var user = JSON.parse(localStorage.getItem('currentUser'));
        return user.id;
    }
}

function setCurrentUserForDevelopment() {
    var user = {
        "id": "1",
        "profile": {
            "id": "2",
            "email": "test@c.com",
            "name": "Mr Test",
            "certified": true,
            "total_schools": 1
        },
        "currentSchool": {
            "id": "2",
            "school_id": "1"
        },
        "tokenData": {
            "type": "bearer",
            "expires_in": 43200,
            "scope": "read write"
        }
    };

    localStorage.currentUser = JSON.stringify(user);
}
