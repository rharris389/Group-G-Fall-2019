from django.urls import path, include
from rest_framework.urlpatterns import format_suffix_patterns
import Api.views as views

urlpatterns = [
    path('AddUser/', views.AddUser, name='AddUser'),
    path('GetUser/<str:username>/', views.GetUser, name='GetUser'),
    path('GetPasswd/<str:username>/', views.GetPasswd, name='GetPasswd'),
    path('AddEvent/', views.AddEvent, name='AddEvent'),
    path('GetAllEvents/', views.GetAllEvents, name='GetAllEvents'),
    path('GetEvents/<str:start>/<str:end>/', views.GetEvents, name='GetEvents'),
    path('GetAllUsers/', views.GetAllUsers, name='GetAllUsers')
]

urlpatterns = format_suffix_patterns(urlpatterns)
