<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/*
  | -------------------------------------------------------------------------
  | URI ROUTING
  | -------------------------------------------------------------------------
  | This file lets you re-map URI requests to specific controller functions.
  |
  | Typically there is a one-to-one relationship between a URL string
  | and its corresponding controller class/method. The segments in a
  | URL normally follow this pattern:
  |
  |	example.com/class/method/id/
  |
  | In some instances, however, you may want to remap this relationship
  | so that a different class/function is called than the one
  | corresponding to the URL.
  |
  | Please see the user guide for complete details:
  |
  |	https://codeigniter.com/user_guide/general/routing.html
  |
  | -------------------------------------------------------------------------
  | RESERVED ROUTES
  | -------------------------------------------------------------------------
  |
  | There are three reserved routes:
  |
  |	$route['default_controller'] = 'welcome';
  |
  | This route indicates which controller class should be loaded if the
  | URI contains no data. In the above example, the "welcome" class
  | would be loaded.
  |
  |	$route['404_override'] = 'errors/page_missing';
  |
  | This route will tell the Router which controller/method to use if those
  | provided in the URL cannot be matched to a valid route.
  |
  |	$route['translate_uri_dashes'] = FALSE;
  |
  | This is not exactly a route, but allows you to automatically route
  | controller and method names that contain dashes. '-' isn't a valid
  | class or method name character, so it requires translation.
  | When you set this option to TRUE, it will replace ALL dashes in the
  | controller and method URI segments.
  |
  | Examples:	my-controller/index	-> my_controller/index
  |		my-controller/my-method	-> my_controller/my_method
 */
$route['default_controller'] = 'login';
$route['404_override'] = '';
$route['translate_uri_dashes'] = FALSE;
$route['login'] = 'login/loginaction';
$route['home'] = 'home/homepage';
$route['home/(:num)'] = 'home/homepage/$1';
$route['amountincr'] = 'home/IncrimentAmount';
$route['amountdinc'] = 'home/DincrimentAmount';
$route['checktime'] = 'home/checktimeforamount';
$route['startlessonf'] = 'home/startlesson';
$route['endlessonf'] = 'home/endlesson';
$route['report'] = 'home/reportpage';
$route['actionreport'] = 'home/getGroupReport';
$route['classes'] = 'home/classes';
$route['actionclasses'] = 'home/getClassReport';
$route['checkonline'] = 'home/CheckOnlineStud';
$route['exit'] = 'home/logout';
$route['teachers'] = 'admin/tetchers';
$route['edit/(:num)'] = 'admin/edituser/$1';
$route['addtogroup'] = 'admin/addtogroup';
$route['ngroups'] = 'admin/ngroups';
$route['closegroup'] = 'admin/closegroup';
$route['opengroup'] = 'admin/opengroup';
$route['delgroup'] = 'admin/delgroup';
$route['creategroup'] = 'admin/creategroups';
$route['scan'] = 'admin/Readcard';
$route['studupdate'] = 'admin/studeintsupdate';
$route['imageupload'] = 'admin/uploadimage';
$route['datasave'] = 'admin/newtetcher';
$route['dataupdate'] = 'admin/dataupdate';
$route['upgradegroup'] = 'admin/upgradegroup';
$route['asadmin'] = 'admin/makeadmin';
$route['asuser'] = 'admin/makeuser';
$route['asblock'] = 'admin/makeblock';
$route['asunblock'] = 'admin/makeunblock';
$route['asdelete'] = 'admin/makedelete';
$route['students'] = 'admin/studentlist';
$route['recreatestudent'] = 'admin/recreatestudent';
$route['createstuding'] = 'admin/createstuding';
$route['studblock'] = 'admin/blockstudent';
$route['studunblock'] = 'admin/unblockstudent';
$route['studdelete'] = 'admin/deletestudent';
$route['opendirectory'] = 'admin/opendirectory';
$route['studaddmoney'] = 'admin/StudentIncrimentAmount';
$route['studgetmoney'] = 'admin/StudentDincrimentAmount';
$route['studaddtogroup'] = 'admin/studentaddtogroup';
$route['setting'] = 'settings/view';
$route['updatesetting'] = 'settings/update';
$route['computers'] = 'settings/ComputerPage';
$route['poweroff'] = 'settings/Computeraction';
$route['poweroffall'] = 'settings/allComputeraction';
$route['tinfo'] = 'admin/getTecherInformations';
$route['ginfo'] = 'admin/getGroupInformations';
$route['stinfo'] = 'admin/getStudentsInformations';
