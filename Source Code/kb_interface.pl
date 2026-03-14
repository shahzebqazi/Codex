:- module(kb_interface, [search_books/2, search_by_keyword/2, get_book_details/2, 
                         list_authors/1, list_categories/1, get_media_files/2]).

:- use_module(library_kb).

% High-level search interface
search_books(Query, Results) :-
    ( string(Query) -> atom_string(QueryAtom, Query) ; QueryAtom = Query ),
    findall(book(C,A,T,P), 
            (book(C,A,T,P), 
             (sub_string(A, _, _, _, QueryAtom) ; 
              sub_string(T, _, _, _, QueryAtom) ; 
              sub_string(C, _, _, _, QueryAtom))), 
            Results).

search_by_keyword(Keyword, Results) :-
    atom_string(KeywordAtom, Keyword),
    findall(book(C,A,T,P), 
            (book(C,A,T,P), 
             (sub_string(A, _, _, _, KeywordAtom) ; 
              sub_string(T, _, _, _, KeywordAtom) ; 
              sub_string(C, _, _, _, KeywordAtom) ;
              meta(C,A,T,_,V), sub_string(V, _, _, _, KeywordAtom))), 
            Results).

% Get detailed information about a specific book
get_book_details(book(C,A,T,P), Details) :-
    findall(meta(Key,Value), meta(C,A,T,Key,Value), MetaData),
    findall(media(Kind,Path), media(C,A,T,Kind,Path), MediaFiles),
    Details = details(
        category(C),
        author(A), 
        title(T),
        path(P),
        metadata(MetaData),
        media(MediaFiles)
    ).

% List all authors in the library
list_authors(Authors) :-
    findall(A, book(_,A,_,_), AuthorList),
    sort(AuthorList, Authors).

% List all categories in the library  
list_categories(Categories) :-
    findall(C, book(C,_,_,_), CategoryList),
    sort(CategoryList, Categories).

% Get all media files for a specific book
get_media_files(book(C,A,T,_), MediaFiles) :-
    findall(media(Kind,Path), media(C,A,T,Kind,Path), MediaFiles).
