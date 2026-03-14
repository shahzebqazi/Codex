:- module(library_kb, [build_kb/1, book/4, media/5, meta/5, query_books/1, query_author/2, query_category/2]).
:- use_module(library(filesex)).
:- use_module(library(readutil)).
:- use_module(library(apply)).
:- use_module(library(os)).

% Dynamic predicates for the knowledge base
:- dynamic book/4.        % book(Category, Author, Title, Path)
:- dynamic media/5.       % media(Category, Author, Title, Kind, Path)
:- dynamic meta/5.        % meta(Category, Author, Title, Key, Value)

% Main entry point to build the knowledge base
build_kb(Root) :-
    retractall(book(_,_,_,_)),
    retractall(media(_,_,_,_,_)),
    retractall(meta(_,_,_,_,_)),
    normalize_path(Root, NormalRoot),
    walk_directory(NormalRoot, visit, [follow_links(false)]).

% Visitor function for directory traversal
visit(Path, _Stat) :-
    file_base_name(Path, '_Book.md'), !,
    process_book_md(Path).
visit(_, _).

% Process individual book metadata files
process_book_md(BookMd) :-
    file_directory_name(BookMd, TitleDir),
    file_directory_name(TitleDir, AuthorDir),
    file_directory_name(AuthorDir, CatDir),
    file_base_name(TitleDir, Title),
    file_base_name(AuthorDir, Author),
    file_base_name(CatDir, Category),
    normalize_path(TitleDir, NormalPath),
    assertz(book(Category, Author, Title, NormalPath)),
    index_media(Category, Author, Title, TitleDir),
    extract_and_assert_meta(Category, Author, Title, BookMd).

% Index media files in the book's media directory
index_media(C, A, T, TitleDir) :-
    directory_file_path(TitleDir, 'media', MediaDir),
    ( exists_directory(MediaDir)
    -> directory_files(MediaDir, Entries),
       exclude(is_dot, Entries, Files),
       maplist(assert_media(C,A,T,MediaDir), Files)
    ; true).

is_dot('.').
is_dot('..').

% Assert media file facts
assert_media(C,A,T,Dir,Name) :-
    file_name_extension(_, Ext0, Name),
    downcase_atom(Ext0, Ext),
    ( Ext = epub -> Kind = epub
    ; Ext = pdf  -> Kind = pdf
    ; Ext = txt  -> Kind = txt
    ; Ext = mp3  -> Kind = audio
    ; Ext = mp4  -> Kind = video
    ; !, fail),
    directory_file_path(Dir, Name, Path),
    normalize_path(Path, NormalPath),
    assertz(media(C,A,T,Kind,NormalPath)), !.
assert_media(_,_,_,_,_).

% Extract and assert metadata from _Book.md files
extract_and_assert_meta(C,A,T,BookMd) :-
    read_file_to_string(BookMd, S, []),
    split_string(S, "\n", "\r", Lines),
    maplist(meta_kv, Lines, KVs0),
    exclude(=(none), KVs0, KVs),
    maplist(assert_kv(C,A,T), KVs).

% Parse key-value pairs from markdown files
meta_kv(Line, none) :-
    ( Line = "" ; sub_string(Line, 0, 1, _, "#") ), !.
meta_kv(Line, Key-Value) :-
    sub_string(Line, P, 1, _, ":"),
    P > 0,
    sub_string(Line, 0, P, _, Key0S),
    P1 is P + 1,
    sub_string(Line, P1, _, 0, Val0S),
    normalize_space(string(Key1S), Key0S),
    normalize_space(string(Val1S), Val0S),
    Key1S \= "", Val1S \= "",
    downcase_atom(Key1S, KeyLower),
    normalize_key(KeyLower, Key),
    Value = Val1S, !.
meta_kv(_, none).

% Normalize keys to valid Prolog atoms
normalize_key(S, KeyAtom) :-
    split_string(S, " /-:", " /-:", Parts0),
    exclude(=(""), Parts0, Parts),
    atomic_list_concat(Parts, '_', KeyAtom).

% Assert metadata key-value pairs
assert_kv(C,A,T,Key-Value) :-
    assertz(meta(C,A,T,Key,Value)).

% Query predicates for the knowledge base
query_books(Books) :-
    findall(book(C,A,T,P), book(C,A,T,P), Books).

query_author(Author, Books) :-
    findall(book(C,A,T,P), book(C,A,T,P), AllBooks),
    include(book_by_author(Author), AllBooks, Books).

query_category(Category, Books) :-
    findall(book(C,A,T,P), book(C,A,T,P), AllBooks),
    include(book_by_category(Category), AllBooks, Books).

% Helper predicates for queries
book_by_author(Author, book(_,A,_,_)) :-
    sub_string(A, _, _, _, Author).

book_by_category(Category, book(C,_,_,_)) :-
    sub_string(C, _, _, _, Category).

% Cross-platform path normalization
normalize_path(Path, NormalPath) :-
    prolog_to_os_filename(Path, OSPath),
    prolog_to_os_filename(NormalPath, OSPath).
