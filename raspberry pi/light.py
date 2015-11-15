#coding=utf-8
#! /usr/bin/python   

import RPi.GPIO as GPIO
import weibo  
import os
import time
import urllib2
import random
import linecache
import urllib2
import urllib
from weibo import APIClient

GPIO.setmode(GPIO.BOARD)
GPIO.setup(11,GPIO.OUT)
  


def run():
	lastmodified = 0
	APP_KEY = "2273916377"
	APP_SECRET = "453f589fdbb92f9923e56d1b29b5ab72"
	USERID = "gengxu.heartfire@gmail.com"
	PASSWD = "Philip1994"
	CALLBACK_URL = 'https://api.weibo.com/oauth2/default.html'
	client = APIClient(app_key=APP_KEY, app_secret=APP_SECRET, redirect_uri=CALLBACK_URL)
	referer_url = client.get_authorize_url()
	print "referer url is : %s" % referer_url
    	cookies = urllib2.HTTPCookieProcessor()
    	opener = urllib2.build_opener(cookies)
    	urllib2.install_opener(opener)
   	postdata = {
		"client_id": APP_KEY,
		"redirect_uri": CALLBACK_URL,
		"userId": USERID,
		"passwd": PASSWD,
		"isLoginSina": "0",	
		"action": "submit",
		"response_type": "code",
		}
 
	headers = {
		"User-Agent": "Mozilla/5.0 (Windows NT 6.1; rv:11.0) Gecko/20100101 Firefox/11.0",
              "Host": "api.weibo.com",
		"Referer": referer_url
		}
 
	req  = urllib2.Request(
		url = referer_url,
		data = urllib.urlencode(postdata),
		headers = headers
		)
	try:
		resp = urllib2.urlopen(req)
		print "callback url is : %s" % resp.geturl()
		print "code is : %s" % resp.geturl()[-32:]
		code=resp.geturl()[-32:]
		r = client.request_access_token(code)  
		client.set_access_token(r.access_token, r.expires_in)
	except Exception, e:
		print e

	while True:
		thetime=time.strftime('%S',time.localtime(time.time())) 
		thetime = int(thetime)
		timemod = thetime % 10
		if timemod == 0:
			msg = client.statuses.user_timeline.get()['statuses'][0]
			try:					
				bitmsg = msg['text']
				msgtime = msg['created_at']
				print bitmsg,
				msgtime = str(msgtime)
				timeArray = time.strptime(msgtime,"%a %b %d %H:%M:%S +0800 %Y") 
				timeStamp = int(time.mktime(timeArray))
				print timeStamp,
				if timeStamp>lastmodified:
					lastmodified = timeStamp
					if bitmsg=='light on':
						GPIO.output(11,True)
					if bitmsg=='light off':
						GPIO.output(11,False)
				
				print lastmodified,
				print "##########################"
			except Exception, e:
				print e
		if timemod == 5:
			f=urllib.urlopen("http://www.comp.polyu.edu.hk/~12132031d/comp3432/lightstatus.php")
			s=f.read()
			bitmsg = s.split(';')[0]
			timeStamp = s.split(';')[1]
			timeStamp = int(timeStamp)
			timeStamp+=28813
			try:					
				print bitmsg,
				print timeStamp
				if timeStamp>lastmodified:
					lastmodified = timeStamp
					if bitmsg=='light on':
						GPIO.output(11,True)
					if bitmsg=='light off':
						GPIO.output(11,False)
				print lastmodified,
				print "##########################"
			except Exception, e:
				print e

		time.sleep(1)
if __name__ == "__main__":  
    run()  

