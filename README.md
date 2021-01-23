# Saving-Hobbits
 Graph traversal algorithm which pops vertices as they are used, finding the maximum amount of legal paths from minimal nodes to maximal node endpoints.

 The problem: Gandalf is trying to save as many hobbits as he can. He begins throwing them across a chasm in Mordor with magical floating platforms engraved with numbers. He has to save as many members of the fellowship as he can, however the platforms obey magical laws that allow for traversal from one to another only if the destination has a number greater than the current platform, and they have a GCD > 1. All Hobbits must start on 1, a platform that never falls, then must move to a minimal platform, and eventually end on a maximal platform. Maximals are defined as platforms whose number has a GCD = 1 with all greater platform values. Minimals are defined as platforms whose number has a GCD = 1 with all lower platform values. It is possible for a platform to be both minimal and maximal ( e.x. a prime number greater than all others values in the set of platforms ).
 
 An input set of platform values 1 5 7 15 49 45 will result in two legal paths. 1 -> 5 -> 45 and 1 -> 7 -> 49.
