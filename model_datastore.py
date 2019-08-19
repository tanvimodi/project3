# Copyright 2015 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
​
from flask import current_app
from google.cloud import datastore
​
​
builtin_list = list
​
​
def init_app(app):
    pass
​
​
def get_client():
    return datastore.Client(current_app.config['PROJECT_ID'])
​
​
# [START from_datastore]
def from_datastore(entity):
    """Translates Datastore results into the format expected by the
    application.
​
    Datastore typically returns:
        [Entity{key: (kind, id), prop: val, ...}]
​
    This returns:
        {id: id, prop: val, ...}
    """
    if not entity:
        return None
    if isinstance(entity, builtin_list):
        entity = entity.pop()
​
    entity['id'] = entity.key.id
    return entity
# [END from_datastore]
​
​
# [START list]
def list(limit=10, cursor=None):
    ds = get_client()
​
    query = ds.query(kind='Event')
    query_iterator = query.fetch(limit=limit, start_cursor=cursor)
    page = next(query_iterator.pages)
​
    entities = builtin_list(map(from_datastore, page))
    next_cursor = (
        query_iterator.next_page_token.decode('utf-8')
        if query_iterator.next_page_token else None)
​
    return entities, next_cursor
# [END list]
​
​
# [START list]
def list_v(limit=10, cursor=None):
    ds = get_client()
​
    query = ds.query(kind='Venue')
    query_iterator = query.fetch(limit=limit, start_cursor=cursor)
    page = next(query_iterator.pages)
​
    entities = builtin_list(map(from_datastore, page))
    next_cursor = (
        query_iterator.next_page_token.decode('utf-8')
        if query_iterator.next_page_token else None)
​
    return entities, next_cursor
# [END list]
​
​
# [START list]
def list_users(limit=10, cursor=None):
    ds = get_client()
​
    query = ds.query(kind='User')
    query_iterator = query.fetch(limit=limit, start_cursor=cursor)
    page = next(query_iterator.pages)
​
    entities = builtin_list(map(from_datastore, page))
    next_cursor = (
        query_iterator.next_page_token.decode('utf-8')
        if query_iterator.next_page_token else None)
​
    return entities, next_cursor
# [END list]
​
​
# [START list]
def list_ev(limit=10, cursor=None):
    ds = get_client()
​
    query = ds.query(kind='Event')
    query_iterator = query.fetch(limit=limit, start_cursor=cursor)
    page = next(query_iterator.pages)
​
    entities = builtin_list(map(from_datastore, page))
    next_cursor = (
        query_iterator.next_page_token.decode('utf-8')
        if query_iterator.next_page_token else None)
​
    return entities, next_cursor

def list_reservations(limit=10, cursor=None):
    ds = get_client()
​
    query = ds.query(kind='Reservation')
    query_iterator = query.fetch(limit=limit, start_cursor=cursor)
    page = next(query_iterator.pages)
​
    entities = builtin_list(map(from_datastore, page))
    next_cursor = (
        query_iterator.next_page_token.decode('utf-8')
        if query_iterator.next_page_token else None)
​
    return entities, next_cursor
# [END list]
​
def read(id):
    ds = get_client()
    key = ds.key('User', int(id))
    results = ds.get(key)
    return from_datastore(results)
​
​
# [START update]
def update(data, id=None):
    ds = get_client()
    if id:
        key = ds.key('User', int(id))
    else:
        key = ds.key('User')
​
    entity = datastore.Entity(
        key=key,
       )
​
    entity.update(data)
    ds.put(entity)
    return from_datastore(entity)
​
def update_reservation(data, id=None):
    ds = get_client()
    if id:
        key = ds.key('Reservation', int(id))
    else:
        key = ds.key('Reservation')
​
    entity = datastore.Entity(
        key=key,
       )
​
    entity.update(data)
    ds.put(entity)
    return from_datastore(entity)
​
​
def update_venue(data, id=None):
    ds = get_client()
    if id:
        key = ds.key('Venue', int(id))
    else:
        key = ds.key('Venue')
​
    entity = datastore.Entity(
        key=key,
       )
​
    entity.update(data)
    ds.put(entity)
    return from_datastore(entity)
​
​
​
create = update
create_reservation = update_reservation
create_venue = update_venue
# [END update]
​
​
def delete(id):
    ds = get_client()
    key = ds.key('User', int(id))
    ds.delete(key)