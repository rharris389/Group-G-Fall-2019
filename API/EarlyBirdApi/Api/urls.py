from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
import Api.views as views

urlpatterns = [
    path("AddUser/", views.AddUser),
    path("EditUser/<str:username>/<str:property>/<str:newData>/", views.EditUser),
    path("EditPasswdForUser/", views.EditPasswdForUser),
    path("GetUser/<str:username>/", views.GetUser),
    path("DeleteUser/<str:username>/", views.DeleteUser),
    path("AddEvent/", views.AddEvent),
    path(
        "EditEventForUser/<str:username>/<str:name>/<str:start>/<str:end>/<str:notification>/<str:frequency>/<str:property>/<str:newData>/",
        views.EditEventForUser
    ),
    path("GetAllEvents/", views.GetAllEvents),
    path("GetEventsInTimeRange/<str:start>/<str:end>/", views.GetEventsInTimeRange),
    path("GetEventsInTimeRangeForUser/<str:username>/<str:start>/<str:end>/", views.GetEventsInTimeRangeForUser),
    path("GetAllUsers/", views.GetAllUsers),
    path("DeleteEventForUser/<str:username>/<str:name>/<str:start>/<str:end>/", views.DeleteEventForUser),
    path("AddGoal/", views.AddGoal),
    path("EditGoalById/<int:id>/<str:property>/<str:newData>/", views.EditGoalById),
    path("DeleteGoalForUser/<str:username>/<str:name>/<str:isCompleted>/<str:notes>/", views.DeleteGoalForUser),
    path("GetGoalById/<int:id>/", views.GetGoalById),
    path("GetAllCompletedGoalsForUser/<str:username>/", views.GetAllCompletedGoalsForUser),
    path("GetAllIncompletedGoalsForUser/<str:username>/", views.GetAllIncompletedGoalsForUser),
    path("AddTimeRestriction/", views.AddTimeRestriction),
    path(
        "EditTimeRestrictionForUser/<str:username>/<str:start>/<str:end>/<str:frequency>/<str:property>/<str:newData>/",
        views.EditTimeRestrictionForUser
    ),
    path(
        "GetTimeRestrictionForUser/<str:username>/<str:start>/<str:end>/<str:frequency>/",
        views.GetTimeRestrictionForUser
    ),
    path(
        "GetTimeRestrictionsInTimeRangeForUser/<str:username>/<str:start>/<str:end>/",
        views.GetTimeRestrictionsInTimeRangeForUser
    ),
    path(
        "GetTimeRestrictionsWithStartAndEndForUser/<str:username>/<str:start>/<str:end>/",
        views.GetTimeRestrictionsWithStartAndEndForUser
    ),
    path(
        "GetTimeRestrictionsWithFrequencyForUser/<str:username>/<str:frequency>/",
        views.GetTimeRestrictionsWithFrequencyForUser
    ),
    path("GetAllTimeRestrictionsForUser/<str:username>/", views.GetAllTimeRestrictionsForUser),
    path(
        "DeleteTimeRestrictionForUser/<str:username>/<str:start>/<str:end>/<str:frequency>/",
        views.DeleteTimeRestrictionForUser
    )
]

urlpatterns = format_suffix_patterns(urlpatterns)
