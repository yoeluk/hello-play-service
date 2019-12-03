## Hello Play Service

### Run

```
> sbt run
```

### Docker build

```
> sbt docker:publishLocal
```

### Docker run

```
> docker run -it -e BEARER_TOKEN=<GCloud Translate Api Toke> -p 9000:9000 yoeluk/hello-play-service:0.3 -Dpidfile.path=/dev/null -Dplay.http.secret.key=ad31779d4ee49d5ad5162bf1429c32e2e9933f3b
```

### Persistence

This sample app uses JPA API from Play for its persistence requirements. The persistence entities (`User` and
 `Greeting`) are configured for different persistence unit. The current configuration is pointing to 2 different
  postgresql dbs `user` and `greeting`. The `default` entity configuration is pointing to H2 but it is not used
  , however, all entities in test are configured to point to H2.

#### User Table (Postgresql)

You can change the configuration to persist to H2 (its dependency and persistent unit are included to make this easy
). Issue the give sql to create your `user_table` in your postgresql instant.

```sql
CREATE TABLE user_table (
	id int8 NOT NULL,
	email varchar(255) NOT NULL,
	full_name varchar(255) NOT NULL,
	CONSTRAINT user_table_pkey PRIMARY KEY (id)
);
CREATE UNIQUE INDEX user_table_email_uindex ON public.user_table USING btree (email);
```

#### Greeting Table (Postgresql)

You can change the configuration to persist to H2 (its dependency and persistent unit are included to make this easy
). Issue the give sql to create your `greeting_table` in your postgresql instant.

```sql
CREATE TABLE greeting_table (
	greeting varchar(255) NOT NULL,
	message varchar(255) NOT NULL,
	CONSTRAINT greeting_table_pk PRIMARY KEY (greeting)
);
CREATE UNIQUE INDEX greeting_table_greeting_uindex ON public.greeting_table USING btree (greeting);
```

### API Sample Requests

#### Add greetinga
```bash
> curl -d '{"greeting":"good morning","message":"you look great today!"}' -H "Content-Type: application/json" localhost:9000/v1/ug/addgreeting
> curl -d '{"greeting":"hello","message":"nice to see you again!"}' -H "Content-Type: application/json" localhost:9000/v1/ug/addgreeting
> curl -d '{"greeting":"good evening","message":"what a pleasant surprise!"}' -H "Content-Type: application/json" localhost:9000/v1/ug/addgreeting
```

#### Get greetings

```bash
> curl -H "Accept: application/json" localhost:9000/v1/ug/allgreetings
```

#### Add users

```bash
> curl -d '{"username":"markdavis","fullName":"Mark Davis","email":"mark.davis@me.com"}' -H "Content-Type: application/json" localhost:9000/v1/ug/adduser
> curl -d '{"username":"johnsmith","fullName":"John Smith","email":"john.smith@me.com"}' -H "Content-Type: application/json" localhost:9000/v1/ug/adduser
```

#### Get user

```bash
> curl localhost:9000/v1/ug/getuser?id=<USER_ID>
```

#### Get usergreeting

```bash
curl localhost:9000/v1/ug/usergreetings/6 | jq
```
```json
{
  "user": {
    "id": 6,
    "fullName": "mark davis",
    "username": "markdavis",
    "email": "mark.davis@me.com"
  },
  "greetings": [
    {
      "greeting": "good afternoon",
      "message": "nice weather"
    },
    {
      "greeting": "hello",
      "message": "you look great today!"
    }
  ]
}
```