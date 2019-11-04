from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
import Api.views as views

urlpatterns = [
    path('AddUser/', views.AddUser, name='AddUser'),
    path('GetUser/<str:username>/', views.GetUser, name='GetUser'),
    path('DeleteUser/<str:username>/', views.DeleteUser, name='DeleteUser'),
    path('AddEvent/', views.AddEvent, name='AddEvent'),
    path('GetAllEvents/', views.GetAllEvents, name='GetAllEvents'),
    path('GetEventsInTimeRange/<str:start>/<str:end>/', views.GetEventsInTimeRange, name='GetEventsInTimeRange'),
    path('GetEventsInTimeRangeForUser/<str:username>/<str:start>/<str:end>/', views.GetEventsInTimeRangeForUser, name='GetEventsInTimeRangeForUser'),
    path('GetAllUsers/', views.GetAllUsers, name='GetAllUsers'),
    path('DeleteEventForUser/<str:username>/<str:name>/<str:start>/<str:end>/', views.DeleteEventForUser, name='DeleteEventForUser'),
    path('AddTimeRestriction/', views.AddTimeRestriction, name='AddTimeRestriction'),
    path('GetTimeRestrictionForUser/<str:username>/<str:start>/<str:end>/<str:frequency>/', views.GetTimeRestrictionForUser, name='GetTimeRestrictionForUser'),
    path('GetTimeRestrictionsInTimeRangeForUser/<str:username>/<str:start>/<str:end>/', views.GetTimeRestrictionsInTimeRangeForUser, name='GetTimeRestrictionInTimeRangeForUser'),
    path('GetTimeRestrictionsWithStartAndEndForUser/<str:username>/<str:start>/<str:end>/', views.GetTimeRestrictionsWithStartAndEndForUser, name='GetTimeRestrictionsWithStartAndEndForUser'),
    path('GetTimeRestrictionsWithFrequencyForUser/<str:username>/<str:frequency>/', views.GetTimeRestrictionsWithFrequencyForUser, name='GetTimeRestrictionsWithFrequencyForUser'),
    path('GetAllTimeRestrictionsForUser/<str:username>/', views.GetAllTimeRestrictionsForUser, name='GetAllTimeRestrictionsForUser'),
    path('DeleteTimeRestrictionForUser/<str:username>/<str:start>/<str:end>/<str:frequency>/', views.DeleteTimeRestrictionForUser, name='DeleteTimeRestrictionForUser')
]

urlpatterns = format_suffix_patterns(urlpatterns)
