:- module(kb_export, [export_to_json/2, export_to_csv/2, export_to_yaml/2]).

:- use_module(library_kb).
:- use_module(kb_interface).

% Export knowledge base to JSON format
export_to_json(OutputFile, Status) :-
    open(OutputFile, write, Stream),
    write(Stream, '['),
    query_books(Books),
    export_books_json(Books, Stream),
    write(Stream, ']'),
    close(Stream),
    Status = exported.

export_books_json([], _).
export_books_json([book(C,A,T,P)|Rest], Stream) :-
    write(Stream, '{'),
    write(Stream, '"category":"'), write(Stream, C), write(Stream, '",'),
    write(Stream, '"author":"'), write(Stream, A), write(Stream, '",'),
    write(Stream, '"title":"'), write(Stream, T), write(Stream, '",'),
    write(Stream, '"path":"'), write(Stream, P), write(Stream, '"'),
    get_media_files(book(C,A,T,P), MediaFiles),
    export_media_json(MediaFiles, Stream),
    export_meta_json(C,A,T, Stream),
    write(Stream, '}'),
    (Rest = [] -> true ; write(Stream, ',')),
    export_books_json(Rest, Stream).

export_media_json([], Stream) :- write(Stream, ',"media":[]').
export_media_json(MediaFiles, Stream) :-
    write(Stream, ',"media":['),
    export_media_list(MediaFiles, Stream),
    write(Stream, ']').

export_media_list([], _).
export_media_list([media(Kind,Path)|Rest], Stream) :-
    write(Stream, '{"kind":"'), write(Stream, Kind), write(Stream, '","path":"'), write(Stream, Path), write(Stream, '"}'),
    (Rest = [] -> true ; write(Stream, ',')),
    export_media_list(Rest, Stream).

export_meta_json(C,A,T, Stream) :-
    findall(Key-Value, meta(C,A,T,Key,Value), MetaList),
    (MetaList = [] -> write(Stream, ',"metadata":{}')
    ; write(Stream, ',"metadata":{'),
      export_meta_list(MetaList, Stream),
      write(Stream, '}')).

export_meta_list([], _).
export_meta_list([Key-Value|Rest], Stream) :-
    write(Stream, '"'), write(Stream, Key), write(Stream, '":"'), write(Stream, Value), write(Stream, '"'),
    (Rest = [] -> true ; write(Stream, ',')),
    export_meta_list(Rest, Stream).

% Export knowledge base to CSV format
export_to_csv(OutputFile, Status) :-
    open(OutputFile, write, Stream),
    write(Stream, 'Category,Author,Title,Path,ISBN,Publisher,Year,Status\n'),
    query_books(Books),
    export_books_csv(Books, Stream),
    close(Stream),
    Status = exported.

export_books_csv([], _).
export_books_csv([book(C,A,T,P)|Rest], Stream) :-
    get_meta_value(C,A,T,isbn, ISBN),
    get_meta_value(C,A,T,publisher, Publisher),
    get_meta_value(C,A,T,year, Year),
    get_meta_value(C,A,T,status, Status),
    write(Stream, C), write(Stream, ','),
    write(Stream, A), write(Stream, ','),
    write(Stream, T), write(Stream, ','),
    write(Stream, P), write(Stream, ','),
    write(Stream, ISBN), write(Stream, ','),
    write(Stream, Publisher), write(Stream, ','),
    write(Stream, Year), write(Stream, ','),
    write(Stream, Status), write(Stream, '\n'),
    export_books_csv(Rest, Stream).

get_meta_value(C,A,T,Key,Value) :-
    meta(C,A,T,Key,Value) -> true ; Value = ''.

% Export knowledge base to YAML format (simplified)
export_to_yaml(OutputFile, Status) :-
    open(OutputFile, write, Stream),
    write(Stream, 'library:\n'),
    query_books(Books),
    export_books_yaml(Books, Stream),
    close(Stream),
    Status = exported.

export_books_yaml([], _).
export_books_yaml([book(C,A,T,P)|Rest], Stream) :-
    write(Stream, '  - category: '), write(Stream, C), write(Stream, '\n'),
    write(Stream, '    author: '), write(Stream, A), write(Stream, '\n'),
    write(Stream, '    title: '), write(Stream, T), write(Stream, '\n'),
    write(Stream, '    path: '), write(Stream, P), write(Stream, '\n'),
    export_meta_yaml(C,A,T, Stream),
    export_books_yaml(Rest, Stream).

export_meta_yaml(C,A,T, Stream) :-
    findall(Key-Value, meta(C,A,T,Key,Value), MetaList),
    maplist(write_meta_yaml(Stream), MetaList).

write_meta_yaml(Stream, Key-Value) :-
    write(Stream, '    '), write(Stream, Key), write(Stream, ': '), write(Stream, Value), write(Stream, '\n').
