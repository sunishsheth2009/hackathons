import facebook
import time
import threading

update_stop = threading.Event()

def fb_login(file_name):
    # TODO: LOG IN
    meta_file = '.' + file_name + '.likes'
    new_file = open(meta_file, 'a')
    new_file.close()
    # TODO: DRAW LIKES
    # threading.Timer(0, update).start()
    update_thread = threading.Thread(target=update).start()

def fb_logout():
    update_stop.set()
    # TODO: LOG OUT
    # TODO: CLEAR LIKES
    return 'waiting on app review'

def fb_share(user_token, vim_line):
    graph = facebook.GraphAPI(user_token)
    # status_id = str(graph.put_object('me', 'feed', message=vim_line, link='http://www.vexal.us/vim/' + str(vim_line))['id']).split('_')[1]
    status_attachment = {"name": "User shared some code from Vim", "link":"http://www.vexal.us/vim/" + str(vim_line)}
    status_id = str(graph.put_wall_post(message=vim_line, attachment=status_attachment)['id']).split('_')[1]
    return status_id

def get_num_likes(status_id):
    status = graph.get_object(status_id)
    return str(len(status['likes']['data']))

def update():
    count = 0
    while(not update_stop.is_set()):
       print count
       count+=1
       update_stop.wait(3)
