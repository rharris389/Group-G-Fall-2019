from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
import Api.views as views

urlpatterns = [
    path('AddUser/', views.AddUser, name='AddUser'),
    path('GetUser/<str:username>/', views.GetUser, name='GetUser'),
    path('DeleteUser/<str:username>/', views.DeleteUser, name='DeleteUser'),
    path('GetPasswd/<str:username>/', views.GetPasswd, name='GetPasswd'),
    path('AddEvent/', views.AddEvent, name='AddEvent'),
    path('GetAllEvents/', views.GetAllEvents, name='GetAllEvents'),
    path('GetEventsInTimeRange/<str:start>/<str:end>/', views.GetEventsInTimeRange, name='GetEventsInTimeRange'),
    path('GetEventsInTimeRangeForUser/<str:username>/<str:start>/<str:end>/', views.GetEventsInTimeRangeForUser, name='GetEventsInTimeRangeForUser'),
    path('GetAllUsers/', views.GetAllUsers, name='GetAllUsers'),
    path('DeleteEventForUser/<str:username>/<str:name>/<str:start>/<str:end>/', views.DeleteEventForUser, name='DeleteEventForUser')
]

urlpatterns = format_suffix_patterns(urlpatterns)
