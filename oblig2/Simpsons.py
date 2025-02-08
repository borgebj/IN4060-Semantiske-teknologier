import sys
from rdflib import Graph, Namespace
from rdflib.namespace import RDF, RDFS, FOAF


g = Graph()

g.print()


def main():
    # Command line arguments
    if len(sys.argv) > 1:
        rdf_path = sys.argv[0]
        out_name = sys.argv[1]
        print(f"Path: {sys.argv[0]}\nOut: {sys.argv[1]}")


if __name__ == "__main__":
    main()

