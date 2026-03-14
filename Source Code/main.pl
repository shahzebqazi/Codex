:- module(main, [main/0, init_library/1, query_library/1]).

:- use_module(library_kb).
:- use_module(kb_interface).
:- use_module(kb_export).

% Main entry point for the library system
main :-
    format('Libra AI Library Knowledge Base~n'),
    format('================================~n~n'),
    format('Commands:~n'),
    format('  init(Path)    - Initialize KB from library path~n'),
    format('  search(Query) - Search books by query~n'),
    format('  authors       - List all authors~n'),
    format('  categories    - List all categories~n'),
    format('  export(Format, File) - Export KB (json|csv|yaml)~n'),
    format('  help          - Show this help~n'),
    format('  quit          - Exit~n~n'),
    library_shell.

% Initialize the library knowledge base
init_library(Path) :-
    format('Building knowledge base from: ~w~n', [Path]),
    build_kb(Path),
    query_books(Books),
    length(Books, Count),
    format('Knowledge base built successfully. Found ~d books.~n', [Count]).

% Interactive query interface
query_library(Query) :-
    (Query = search(SearchTerm) ->
        search_books(SearchTerm, Results),
        format('Search results for "~w":~n', [SearchTerm]),
        maplist(format_book, Results)
    ; Query = authors ->
        list_authors(Authors),
        format('Authors in library:~n'),
        maplist(format_author, Authors)
    ; Query = categories ->
        list_categories(Categories),
        format('Categories in library:~n'),
        maplist(format_category, Categories)
    ; Query = export(Format, File) ->
        (Format = json -> export_to_json(File, _)
        ; Format = csv -> export_to_csv(File, _)
        ; Format = yaml -> export_to_yaml(File, _)
        ; format('Unknown export format: ~w~n', [Format])),
        format('Exported to ~w in ~w format~n', [File, Format])
    ; Query = help ->
        main
    ; Query = quit ->
        halt
    ; format('Unknown command: ~w~n', [Query])).

% Simple shell interface
library_shell :-
    format('libra> '),
    read_line_to_string(user_input, Input),
    (Input = "quit" -> halt
    ; Input = "help" -> main, library_shell
    ; Input = "authors" -> list_authors(_), library_shell
    ; Input = "categories" -> list_categories(_), library_shell
    ; atom_string(Command, Input) ->
        query_library(Command), library_shell
    ; format('Invalid input~n'), library_shell).

% Formatting predicates
format_book(book(C,A,T,P)) :-
    format('  ~w by ~w (~w)~n', [T, A, C]),
    format('    Path: ~w~n', [P]).

format_author(Author) :-
    format('  ~w~n', [Author]).

format_category(Category) :-
    format('  ~w~n', [Category]).
