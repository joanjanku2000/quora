
 create table users
  (
      id serial not null,
      first_name character varying(120) not null,
      last_name character varying(120) not null,
      birthdate timestamp without time zone not null,
      gender character varying(10),
      birth_place character varying(50),
      email character varying(100),
 	 user_role character varying(100),
      username character varying(255),
      password character varying(255),
      active boolean,
 	 created_at timestamp without time zone,
 	 updated_at timestamp without time zone,
      constraint user_id_pkey primary key (id)
  );
  create table category(
  	id serial not null,
  	category_name character varying(100),
 	 created_at timestamp without time zone ,
  	created_by integer,
  	updated_by integer,
  	updated_at timestamp without time zone,
  	constraint category_pkey primary key(id),
 	 constraint updated_fkey foreign key(updated_by)
 	references users(id) match simple
  	 on update cascade on delete restrict,
 		constraint created_fkey foreign key(created_by)
 	references users(id) match simple
  	 on update cascade on delete restrict
	
  );
  create table user_group
  (
      id serial not null,
      group_name character varying(100) not null,
  	description character varying(100),
  	created_at timestamp without time zone ,
  	created_by integer,
  	updated_by integer,
  	updated_at timestamp without time zone,
  	id_administrator integer,
  	id_category integer,
      active boolean,
      constraint group_id_pkey primary key (id),
  	constraint admin_fkey foreign key(id_administrator)
  		references users(id) match simple
  	 on update cascade on delete restrict,
  	constraint category_fkey foreign key (id_category)
  	 references category(id) match simple
  	on update cascade on delete restrict,
 	 constraint updated_fkey foreign key(updated_by)
 	references users(id) match simple
  	 on update cascade on delete restrict,
 		constraint created_fkey foreign key(created_by)
 	references users(id) match simple
  	 on update cascade on delete restrict
	
 );

 create table user_group_connection
 (
 id_user integer,
 id_group integer,
 constraint user_fkey foreign key(id_user)
 references users(id) match simple
 on update cascade on delete restrict,
 constraint group_fkey foreign key(id_group)
 	references user_group(id) match simple
  	 on update cascade on delete restrict
  );
 create table tag 
 (
 	id serial not null,
 	tag_name character varying(60),
 	created_by integer,
 	created_at timestamp without time zone,
 	updated_by integer,
 	updated_at timestamp without time zone,
 	constraint tag_pkey primary key(id),
 	constraint updated_fkey foreign key(updated_by)
 	references users(id) match simple
  	 on update cascade on delete restrict,
 		constraint created_fkey foreign key(created_by)
 	references users(id) match simple
  	 on update cascade on delete restrict
	
 );

 create table question
 (
 	id serial not null,
 	question character varying (255),
 	id_user integer not null,
 	id_group integer not null,
 	date timestamp without time zone,
 	updated_at timestamp without time zone not null,
 	constraint pkey primary key(id),
 	constraint user_fkey foreign key(id_user)
 		references users(id) match simple
 		on update cascade on delete restrict,
 	constraint group_fkey foreign key(id_group)
 		references user_group(id) match simple
 		on update cascade on delete restrict
	
 );

 create table question_tags
 (	
 	id_tag integer,
 	id_question integer,
	
 	constraint tag_fkey foreign key(id_tag)
 	references tag(id) match simple
 	on delete restrict on update cascade
 );
create table upvotes_question
(
	id_user integer,
	id_question integer,
	upvote_date timestamp without time zone,
	active boolean,
	
 constraint user_fkey foreign key(id_user)
 	references users(id) match simple
 	on update cascade on delete restrict,
 constraint question_fkey foreign key(id_question)
 	references question(id) match simple
 	on update cascade on delete restrict
);
create table reply
(
	id serial not null,
	id_user integer,
	id_question integer,
	reply character varying(255),
	active boolean ,
	constraint r_pkey primary key (id),
	constraint user_fkey foreign key(id_user)
 	references users(id) match simple
 	on update cascade on delete restrict,
 constraint question_fkey foreign key(id_question)
 	references question(id) match simple
 	on update cascade on delete restrict
);

create table upvotes_reply
(
	id_user integer,
	id_reply integer,
	upvote_date timestamp without time zone,
	active boolean,
	
 constraint user_fkey foreign key(id_user)
 	references users(id) match simple
 	on update cascade on delete restrict,
 constraint reply_fkey foreign key(id_reply)
 	references reply(id) match simple
 	on update cascade on delete restrict
);

