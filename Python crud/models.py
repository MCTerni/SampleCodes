"""
Model for cst8333 project.
Author: Mateus Carnevalli Terni
Version: 20200303
Reference for Singleton in Python:
    Eckel, B. & Friends, 'The Singleton'
    [Online]. Available: https://python-3-patterns-idioms-test.readthedocs.io/en/latest/Singleton.html
    [Accessed: 03-Mar-2020]

"""
from django.db import models
import csv

class Records(models.Model):
    """
    Model with all fields from the csv dataset used for 20W research language project.
    """

    ref_date = models.IntegerField()  # reference date
    geo = models.CharField(max_length=30)  # country
    dguid = models.CharField(max_length=10,null=True,blank=True)  # nullable field, unknow purpose
    sex = models.CharField(max_length=10)  # gender
    age_group = models.CharField(max_length=10,)  # age group
    student_response = models.CharField(max_length=255)  # student response
    uom = models.CharField(max_length=10)  # unkown purpose
    uom_id = models.IntegerField()  # unkown purpose
    scalar_factor = models.CharField(max_length=10)  # unkown purpose
    scalar_id = models.IntegerField()  # unkown purpose
    vector = models.CharField(max_length=10)  # unkown purpose
    coordinate = models.CharField(max_length=10)  # unkown purpose
    value = models.IntegerField()  # unkown purpose
    status = models.CharField(max_length=10, null=True,blank=True)  # nullable field
    symbol = models.CharField(max_length=10, null=True,blank=True)  # nullable field
    terminated = models.CharField(max_length=10, null=True,blank=True)  # nullable field
    decimals = models.IntegerField()  #decimals

   
    

