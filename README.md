User Manual

1 **login page** 

- default username: admin, password: 1234

![1663687944898](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663687944898.png)



2 **home page** 

- home page includes user list and role list 

- if not login, will redirect to the login page
- user can only access the APIs (add, edit, delete, read) based on the roles assigned
- admin got 4 roles including create, delete, edit, read so that can access all the APIs mentioned above

![1663688452297](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663688452297.png)

3 **add user** 

- will add failed if username duplicated 

![1663689466608](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663689466608.png)

![1663689062796](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663689062796.png)



4 **edit user**: **add role to user / delete role to user**  

   ![1663689941822](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663689941822.png)

   ![1663690039490](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663690039490.png)



5 **delete user** 

   ![1663690171110](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663690171110.png)



6 **add role** 

   - will fail if role name duplicated 

     ![1663692288583](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663692288583.png)

   ![1663692925260](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663692925260.png)

   

   - after adding the role in role list,   the new role can also be available to be added to user in user list 

   ![1663693009166](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663693009166.png)

   ![1663693345243](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663693345243.png)



7 **delete role** 

   - will also delete the role associated with users (the role xxx created in the previous step has been deleted)

   ![1663693369237](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663693369237.png)

   ![1663693397443](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663693397443.png)

8 **authorization** 

   - eg when delete the edit role for admin, the edit API will no longer be available

   ![1663693588408](https://github.com/Eva-He/HSBC-TEST/blob/master/doc/image/1663693588408.png)

