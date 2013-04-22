from django.db import models

# Currently, just list all sports here
# Maybe it'd better be stored in table,
# and related to user's interest
SPORT_CHOICES=(
    ('0', 'Basketball'),
    ('1', 'Footbal'),
    ('2', 'Tennis'),
    ('3', 'Biking'),
)


# specify orignal pic or thumbs
IMAGE_TYPE=(
    ('0', '0'),   # orignal pic
    ('1', '128'), # 128 X 128 thumb
    ('2', '256'), # 256 X 256 thumb
)
class Party(models.Model):
    initiator_user = models.ForeignKey('auth.User', related_name='parties')
    title       = models.CharField(max_length='50', blank=True)
    #category    = models.PositiveSmallIntegerField(blank=True)
    # TODO: related to PartyType
    category    = models.CharField(choices=SPORT_CHOICES,
                                    default='Basketball',
                                    max_length=100)
    start_time  = models.DateTimeField(auto_now_add=True)
    end_time    = models.DateTimeField()
    description = models.TextField(max_length='500', blank=True)
    # TODO: stadium
    location    = models.CharField(max_length='100', blank=True)
    #TODO
    image_urls  = models.CharField(max_length='4000', blank=True)

    #user_party  = models.ManyToManyField(User, related_name='user_party', through='UserParty')
    participants = models.ManyToManyField('auth.User', related_name='attended_parties')
    likers = models.ManyToManyField('auth.User', related_name='liked_parties')

    class Meta:
        ordering = [ '-start_time' ]

    def get_participants_num(self):
        return self.participants.count()

    def get_likers_num(self):
        return self.likers.count()

    def get_thumbnail(self):
        return self.image_urls.split(',')[0]


SEX_CHOICES=(('female', 'Female'), ('male', 'Male'), ('secret', 'Secret'))

class UserInfo:
    import datetime

    uid = models.OneToOneField('auth.User', primary_key=True)
    username = models.CharField(max_length='40')
    nickname = models.CharField(max_length='40', blank=True)
    email = models.EmailField()
    sex = models.CharField(choices=SEX_CHOICES, default='Female')
    phone_num = models.CharField(max_length='20')
    birthday = models.DateField(default=datetime.date(1985, 1, 1))
    qq = models.CharField(max_length='20')
    weibo = models.CharField(max_length='40')
    # TODO: What relationship to PartyType?
    #interests = models.ForeignKey(PartyType)
    address = models.TextField(max_length='200')
    company = models.CharField(max_length='200')
    school = models.CharField(max_length='200')
    # TODO: What purpose?
    major = models.CharField(max_length='200')
    # TODO
    image_urls = models.CharField(max_length='4000', blank=True)

    class Meta():
        ordering = ('username')

class PartyType:
    name = models.CharField(max_length='20')
    description = models.TextField(max_length='400')
    calorie_per_hour = models.PositiveSmallIntegerField()

class UserImages:
    user = models.ForeignKey('auth.User', related_name='user_images')
    url  = models.CharField(max_length=4000 )

class PartyImages:
    party = models.ForeignKey(Party, related_name='party_images')
    url   = models.CharField(max_length=4000)

class UserImages(models.Model):
    user = models.ForeignKey('auth.User', related_name='user_images')
    date = models.DateTimeField(auto_now_add = True)
    title  = models.CharField(max_length=100)
    image  = models.CharField(max_length=4000 )
    imgtype = models.CharField(max_length=2)


class PartyImages(models.Model):
    party = models.ForeignKey(Party, related_name='party_images')
    date = models.DateTimeField(auto_now_add = True)
    title  = models.CharField(max_length=100)
    image  = models.CharField(max_length=4000 )
    imgtype = models.CharField(max_length=2)
