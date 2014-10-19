import facebook

def fb_login():
    return 'work in progress'

def fb_share(user_token, vim_line):
    graph = facebook.GraphAPI(user_token)
    profile = graph.get_object('me')
    friends = graph.get_connections('me', 'friends')
    graph.put_object('me', 'feed', message=vim_line, link='http://www.vexal.us/vim')
    return 'sharing to facebook'
