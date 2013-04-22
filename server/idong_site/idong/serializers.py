from rest_framework import serializers
from idong.models import Party, UserImages, PartyImages
from django.contrib.auth.models import User

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', )

class PartyBriefSerializer(serializers.ModelSerializer):
    #participant_num = serializers.Field(source='participants.all().count()')
    participant_num = serializers.Field(source='get_participants_num')
    liker_num = serializers.Field(source='get_likers_num')
    #thumbnail = serializers.Field(source='image_urls[0]')
    thumbnail = serializers.Field(source='get_thumbnail')
    class Meta:
        model = Party
        fields = ('id', 'title', 'category', 'start_time', 'end_time', 'location',
            'participant_num', 'liker_num', 'thumbnail')

class PartyDetailSerializer(serializers.ModelSerializer):
    participant_num = serializers.Field(source='get_participants_num')
    liker_num = serializers.Field(source='get_likers_num')
    initiator_user = serializers.Field(source='initiator_user.username')
    class Meta:
        model = Party
        # TODO: Include participants name list
        # to show who also participate
        fields = ('id', 'title', 'category', 'start_time', 'end_time', 'location',
            'participant_num', 'liker_num', 'image_urls', 'initiator_user', 'description', )

from rest_framework import serializers
class PartySerializer(serializers.Serializer):
    initiator_user = serializers.CharField(max_length=20)
    title = serializers.CharField(max_length=50)
    end_time = serializers.DateField()

    def restore_object(self, attrs, instance=None):
        if 'initiator_user' in attrs:
            user = User.objects.get(username=attrs['initiator_user'])
            attrs['initiator_user'] = user
        if instance is not None:
            instance.title = attrs.get('title', instance.title)
            return instance
        return Party(**attrs)
        
class UserImagesSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserImages
        fields = ('id', 'user', 'date','title', 'image', 'imgtype')

class PartyImagesSerializer(serializers.ModelSerializer):
    class Meta:
        model = PartyImages
        fields = ('id', 'party', 'date','title', 'image', 'imgtype')        