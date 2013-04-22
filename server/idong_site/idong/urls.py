from django.conf.urls import patterns, include, url
from rest_framework.urlpatterns import format_suffix_patterns
from idong import views, images
from idong.models import Party

from django.views.generic import DetailView, ListView

urlpatterns = patterns('',
    url(r'^users/$', views.UserList.as_view()),
    url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view(), name='user_detail'),
    url(r'^parties/$', views.PartyList.as_view()),
    url(r'^parties/(?P<pk>\d+)/$', views.PartyDetail.as_view()),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    #url(r'^select$', images.select),
    url(r'^upload$', images.upload),
    url(r'^adduserimg/$', views.UserImages.as_view()),
    url(r'^userimg/user=(?P<user>[0-9]+)/$', views.UserImages.as_view()),
    url(r'^partyimg/party=(?P<party>[0-9]+)/$', views.PartyImages.as_view()),
    url(r'^addpartyimg/$', views.PartyImages.as_view()),

    url(r'^web-parties/$',
        ListView.as_view(
            model=Party,
            context_object_name='all_parties',
            template_name='idong/index.html'),
        name='web_list'),
    url(r'^web-detail/(?P<pk>\d+)/$',
        DetailView.as_view(
            model=Party,
            template_name='idong/detail.html'),
        name='web_detail'),
    #url(r'^web/$', include('idong.web_urls', namespace='web')),
    #url(r'^web/$', views.PartyList.as_view()),
)

urlpatterns = format_suffix_patterns(urlpatterns)
