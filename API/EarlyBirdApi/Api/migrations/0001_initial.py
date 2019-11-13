# Generated by Django 2.2.7 on 2019-11-13 18:50

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('Id', models.AutoField(primary_key=True, serialize=False)),
                ('Username', models.CharField(max_length=50, unique=True)),
                ('Email', models.CharField(max_length=50, unique=True)),
                ('Passwd', models.CharField(max_length=225)),
                ('Gender', models.CharField(max_length=15, null=True)),
                ('FirstName', models.CharField(max_length=50)),
                ('LastName', models.CharField(max_length=50)),
            ],
        ),
        migrations.CreateModel(
            name='TimeRestriction',
            fields=[
                ('Id', models.AutoField(primary_key=True, serialize=False)),
                ('StartDate', models.DateTimeField()),
                ('EndDate', models.DateTimeField(null=True)),
                ('Frequency', models.CharField(max_length=15)),
                ('UserId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Api.User')),
            ],
        ),
        migrations.CreateModel(
            name='Goal',
            fields=[
                ('Id', models.AutoField(primary_key=True, serialize=False)),
                ('Name', models.CharField(max_length=50)),
                ('IsCompleted', models.BooleanField()),
                ('Notes', models.CharField(max_length=300)),
                ('UserId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Api.User')),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('Id', models.AutoField(primary_key=True, serialize=False)),
                ('Name', models.CharField(max_length=50)),
                ('StartDate', models.DateTimeField()),
                ('EndDate', models.DateTimeField(null=True)),
                ('NotificationDate', models.DateTimeField(null=True)),
                ('Frequency', models.CharField(max_length=15)),
                ('UserId', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='Api.User')),
            ],
        ),
    ]
