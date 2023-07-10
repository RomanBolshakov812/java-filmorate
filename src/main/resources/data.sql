insert into genres ("name") values ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');
insert into mpa ("name") values ('G'), ('PG'), ('PG-13'), ('R'), ('NC_17');

insert into users (name, login, email, birthday) values ('Василий Петров', 'vasia', 'vasia@mail.ru', TO_DATE('17/12/2000', 'DD/MM/YYYY'));
insert into users (name, login, email, birthday) values ('Бенджамин Подковыкин', 'bndj', 'bp@yandex.ru', TO_DATE('23/01/1998', 'DD/MM/YYYY'));
insert into users (name, login, email, birthday) values ('Агафья Смитт', 'agafa', 'agafa@mail.ru', TO_DATE('12/04/2002', 'DD/MM/YYYY'));
insert into users (name, login, email, birthday) values ('Петр Хрюкин', 'hru', 'hruhru@rambler.ru', TO_DATE('06/06/2006', 'DD/MM/YYYY'));
insert into users (name, login, email, birthday) values ('Саша Пушкин', 'push', 'push@mail.ru', TO_DATE('12/04/2002', 'DD/MM/YYYY'));
insert into users (name, login, email, birthday) values ('Лев Хомяков', 'lev', 'lev@rambler.ru', TO_DATE('06/06/2006', 'DD/MM/YYYY'));

insert into films (name, description, release_date, duration, mpa) values ('Восстание ягнят', 'Крутой фильм', TO_DATE('06/06/1984', 'DD/MM/YYYY'), 180, 2);
insert into films (name, description, release_date, duration, mpa) values ('Путь в никуда', 'Депрессняк', TO_DATE('15/02/1999', 'DD/MM/YYYY'), 150, 4);
insert into films (name, description, release_date, duration, mpa) values ('Лучший из худших', 'Крутой боевик', TO_DATE('22/09/2016', 'DD/MM/YYYY'), 120, 5);
insert into films (name, description, release_date, duration, mpa) values ('Кривой баобаб', 'Фентези', TO_DATE('04/10/2022', 'DD/MM/YYYY'), 55, 3);
